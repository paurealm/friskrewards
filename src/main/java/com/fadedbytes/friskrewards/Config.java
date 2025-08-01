package com.fadedbytes.friskrewards;

import net.minecraft.commands.CommandFunction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.ServerFunctionManager;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = FriskRewards.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.BooleanValue ENABLE_REWARDS = BUILDER.comment("Enable Frisk rewards").define("enable_rewards", true);
    private static final ForgeConfigSpec.ConfigValue<String> SIMPLE_ADVANCEMENT_REWARD = BUILDER.comment("Reward function for simple advancements").define("simple_reward", "coinrewards:basic_reward");
    private static final ForgeConfigSpec.ConfigValue<String> CHALLENGE_ADVANCEMENT_REWARD = BUILDER.comment("Reward function for challenge advancements").define("challenge_reward", "coinrewards:special_reward");

    static final ForgeConfigSpec SPEC = BUILDER.build();

    public static boolean enabled;

    public static String simpleRewardFunctionId;
    public static String challengeRewardFunctionId;

    private static CommandFunction.CacheableFunction simpleAdvancementsFunction;
    private static CommandFunction.CacheableFunction challengeAdvancementsFunction;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        enabled = ENABLE_REWARDS.get();

        simpleRewardFunctionId = SIMPLE_ADVANCEMENT_REWARD.get();
        challengeRewardFunctionId = CHALLENGE_ADVANCEMENT_REWARD.get();

        simpleAdvancementsFunction = new CommandFunction.CacheableFunction(ResourceLocation.tryParse(simpleRewardFunctionId));
        challengeAdvancementsFunction = new CommandFunction.CacheableFunction(ResourceLocation.tryParse(challengeRewardFunctionId));
    }

    public static CommandFunction resolveSimpleAdvancementsFunction(ServerFunctionManager functionManager) {
        return simpleAdvancementsFunction.get(functionManager).orElse(null);
    }

    public static CommandFunction resolveChallengeAdvancementsFunction(ServerFunctionManager functionManager) {
        return challengeAdvancementsFunction.get(functionManager).orElse(null);
    }
}
