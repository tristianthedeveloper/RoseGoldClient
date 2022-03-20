package rosegoldclient.mixins;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import rosegoldclient.events.PlayerMoveEvent;
import rosegoldclient.events.ScreenClosedEvent;

@Mixin(EntityPlayerSP.class)
public class MixinEntityPlayerSP {

    @Inject(method = "pushOutOfBlocks", at = @At(value = "HEAD"), cancellable = true)
    public void pushOutOfBlocks(double d2, double f, double blockpos, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false);
    }

    @Inject(method = "closeScreen", at = @At("HEAD"), cancellable = true)
    public void closeScreen(CallbackInfo cir) {
        if (MinecraftForge.EVENT_BUS.post(new ScreenClosedEvent())) cir.cancel();
    }

    @Inject(method = "onUpdateWalkingPlayer", at = @At("HEAD"), cancellable = true)
    public void onUpdateWalking(CallbackInfo cir) {
        if (MinecraftForge.EVENT_BUS.post(new PlayerMoveEvent.Pre())) cir.cancel();
    }

    @Inject(method = "onUpdateWalkingPlayer", at = @At("RETURN"), cancellable = true)
    public void onWalking(CallbackInfo cir) {
        if (MinecraftForge.EVENT_BUS.post(new PlayerMoveEvent.Post())) cir.cancel();
    }
}