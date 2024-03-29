package ca.samueltaylor.simple_commands.commands;

import ca.samueltaylor.simple_commands.abstract_commands.OptionalStringArgument;
import ca.samueltaylor.simple_commands.helpers.Chat;
import ca.samueltaylor.simple_commands.points.HomePoint;
import ca.samueltaylor.simple_commands.points.HomePointManager;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;

import java.util.Objects;

public class Home extends OptionalStringArgument {
    public Home() {
        command = "home";
        argumentName = "HomeName";
    }

    @Override
    public int execute(CommandContext<ServerCommandSource> commandContext) throws CommandSyntaxException {
        return this.run(commandContext, "home");
    }

    @Override
    public int executeWithArgument(CommandContext<ServerCommandSource> commandContext) throws CommandSyntaxException {
        return this.run(commandContext, StringArgumentType.getString(commandContext, argumentName));
    }

    protected int run(CommandContext<ServerCommandSource> commandContext, String homeName) throws CommandSyntaxException {
        PlayerEntity player = commandContext.getSource().getPlayer();
        HomePointManager homePointManager = HomePointManager.instance();
        HomePoint home = homePointManager.getHome(player, homeName);
        Chat chat = new Chat(player);

        if(home != null) {
            home.teleport(player, Objects.requireNonNull(player.getServer()).getWorld(home.location.dimension));
            chat.send("Teleported to your home " + homeName);

            this.logCommand(commandContext);
            return Command.SINGLE_SUCCESS;
        }

        chat.send(homeName + " not found!");
        chat.send("Your homes: " + homePointManager.listHomes(player));
        return Command.SINGLE_SUCCESS;

    }
}
