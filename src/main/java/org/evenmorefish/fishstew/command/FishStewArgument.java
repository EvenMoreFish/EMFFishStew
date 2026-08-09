package org.evenmorefish.fishstew.command;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.MessageComponentSerializer;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import net.kyori.adventure.text.Component;
import org.evenmorefish.fishstew.item.FishStewItem;
import org.evenmorefish.fishstew.item.FishStewRegistry;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.CompletableFuture;

@SuppressWarnings("UnstableApiUsage")
public class FishStewArgument implements CustomArgumentType.Converted<FishStewItem, String> {

    private static final DynamicCommandExceptionType UNKNOWN_TYPE = new DynamicCommandExceptionType(
        obj -> MessageComponentSerializer.message().serialize(Component.text("Unknown FishStewItem: " + obj))
    );

    @Override
    public FishStewItem convert(String nativeType) throws CommandSyntaxException {
        FishStewItem item = FishStewRegistry.getInstance().get(nativeType);
        if (item == null) {
            throw UNKNOWN_TYPE.create(nativeType);
        }
        return item;
    }

    @NonNull
    @Override
    public ArgumentType<String> getNativeType() {
        return StringArgumentType.string();
    }

    @NonNull
    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(@NonNull CommandContext<S> context, @NonNull SuggestionsBuilder builder) {
        FishStewRegistry.getInstance().getRegistry().keySet().stream()
            .filter(name -> name.toLowerCase().startsWith(builder.getRemainingLowerCase()))
            .forEach(builder::suggest);
        return builder.buildFuture();
    }

}