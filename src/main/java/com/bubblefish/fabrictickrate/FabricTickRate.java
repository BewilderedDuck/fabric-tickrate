package com.bubblefish.fabrictickrate;

import com.mojang.brigadier.arguments.FloatArgumentType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.text.Text;

public class FabricTickRate implements ModInitializer {

    private static float customTickrate = 20.0F;

    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) ->
            dispatcher.register(CommandManager.literal("tickrate")
                .requires(source -> source.hasPermissionLevel(2))

                .then(CommandManager.literal("get").executes(context -> {
                    context.getSource().sendFeedback(
                        () -> Text.literal("Current TPS: " + customTickrate),
                        false
                    );
                    return 1;
                }))

                .then(CommandManager.literal("set")
                    .then(CommandManager.argument("value", FloatArgumentType.floatArg())
                        .executes(context -> {
                            customTickrate = FloatArgumentType.getFloat(context, "value");

                            context.getSource().sendFeedback(
                                () -> Text.literal("TPS Changed to: " + customTickrate),
                                true
                            );
                            return 1;
                        })
                    )
                )
            )
        );
    }
    
    private static float oldTPSMaster = 20.0F;

    public static void updateTPSMaster() {
        if (customTickrate != oldTPSMaster) {
            TPSMaster.change(customTickrate);
            oldTPSMaster = customTickrate;
        }
    }
}
