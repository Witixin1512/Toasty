package net.witixin.toasty.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.witixin.toasty.screen.SimpleToastEditScreen;

public class OpenToastEditScreenCommand {

    public static int executeCommand() {
        Minecraft.getInstance().setScreen(new SimpleToastEditScreen());
        return 1;
    }

    public static void register(CommandDispatcher<CommandSourceStack> stack) {
        stack.register(Commands.literal("toast_maker").executes(context -> executeCommand()));
    }
}
