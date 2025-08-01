package com.fadedbytes.friskrewards;

import com.mojang.logging.LogUtils;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.advancements.FrameType;
import net.minecraft.commands.CommandFunction;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerFunctionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(FriskRewards.MODID)
public class FriskRewards {

    public static final String MODID = "friskrewards";
    private static final Logger LOGGER = LogUtils.getLogger();

    public FriskRewards(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();

        modEventBus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.register(this);

        context.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("Setting up Frisk Rewards");
        LOGGER.info("Using function \"{}\" for simple advancements; \"{}\" for challenges", Config.simpleRewardFunctionId, Config.challengeRewardFunctionId);
    }

    @SubscribeEvent
    public void onAdvancementEarned(AdvancementEvent.AdvancementEarnEvent event) {
        if (!Config.enabled) return;

        Advancement advancement = event.getAdvancement();

        DisplayInfo displayInfo = advancement.getDisplay();
        if (displayInfo != null) {
            boolean isChallenge = displayInfo.getFrame() == FrameType.CHALLENGE;

            MinecraftServer server = event.getEntity().getServer();
            if (server != null) {
                ServerFunctionManager functionManager = event.getEntity().getServer().getFunctions();

                CommandFunction rewardFunction = isChallenge
                        ? Config.resolveChallengeAdvancementsFunction(functionManager)
                        : Config.resolveSimpleAdvancementsFunction(functionManager);

                if (rewardFunction != null) {
                    functionManager.execute(rewardFunction, event.getEntity().createCommandSourceStack().withSuppressedOutput().withPermission(2));
                }
            }
        }
    }
}
