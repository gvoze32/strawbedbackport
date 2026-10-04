package com.example.strawbed.mixin;

// Vanilla sets the respawn point inside startSleepInBed; skip that while a
// straw bed sleep attempt is in progress. NeoForge and Forge use
// PlayerSetSpawnEvent instead (see StrawBedMod).
//? if fabric {
/*import com.example.strawbed.world.StrawBedTracker;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//? if <1.21.5 {
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
//?}

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin {
    //? if <1.21.5 {
    @Inject(method = "setRespawnPosition", at = @At("HEAD"), cancellable = true)
    private void strawbed$preserveSpawn(ResourceKey<Level> dimension, BlockPos pos, float angle,
                                         boolean forced, boolean sendMessage, CallbackInfo callbackInfo) {
        strawbed$cancelDuringStrawBedSleep(callbackInfo);
    }
    //?} else {
    @Inject(method = "setRespawnPosition", at = @At("HEAD"), cancellable = true)
    private void strawbed$preserveSpawn(ServerPlayer.RespawnConfig config, boolean sendMessage,
                                         CallbackInfo callbackInfo) {
        strawbed$cancelDuringStrawBedSleep(callbackInfo);
    }
    //?}

    private void strawbed$cancelDuringStrawBedSleep(CallbackInfo callbackInfo) {
        if (StrawBedTracker.shouldCancelSpawnSet((Player) (Object) this)) {
            callbackInfo.cancel();
        }
    }
}
*///?}
