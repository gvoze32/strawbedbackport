package com.example.strawbed.mixin;

//? if fabric {
import com.example.strawbed.world.StrawBedTracker;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin {
    //? if <26.2 {
    @Inject(method = "setRespawnPosition", at = @At("HEAD"), cancellable = true)
    private void strawbed$preserveSpawn(ResourceKey<Level> dimension, BlockPos pos, float angle,
                                         boolean forced, boolean sendMessage, CallbackInfo callbackInfo) {
    //?} else {
    /*@Inject(method = "setRespawnPosition", at = @At("HEAD"), cancellable = true)
    private void strawbed$preserveSpawn(ServerPlayer.RespawnConfig config, boolean sendMessage,
                                         CallbackInfo callbackInfo) {
    *///?}
        if (StrawBedTracker.shouldCancelSpawnSet((Player) (Object) this)) {
            callbackInfo.cancel();
        }
    }
}
//?}
