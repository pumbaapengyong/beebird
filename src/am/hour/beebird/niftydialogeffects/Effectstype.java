package am.hour.beebird.niftydialogeffects;

import am.hour.beebird.niftydialogeffects.effects.BaseEffects;
import am.hour.beebird.niftydialogeffects.effects.FadeIn;
import am.hour.beebird.niftydialogeffects.effects.Fall;
import am.hour.beebird.niftydialogeffects.effects.FlipH;
import am.hour.beebird.niftydialogeffects.effects.FlipV;
import am.hour.beebird.niftydialogeffects.effects.NewsPaper;
import am.hour.beebird.niftydialogeffects.effects.RotateBottom;
import am.hour.beebird.niftydialogeffects.effects.RotateLeft;
import am.hour.beebird.niftydialogeffects.effects.Shake;
import am.hour.beebird.niftydialogeffects.effects.SideFall;
import am.hour.beebird.niftydialogeffects.effects.SlideBottom;
import am.hour.beebird.niftydialogeffects.effects.SlideLeft;
import am.hour.beebird.niftydialogeffects.effects.SlideRight;
import am.hour.beebird.niftydialogeffects.effects.SlideTop;
import am.hour.beebird.niftydialogeffects.effects.Slit;

/**
 * Created by lee on 2014/7/30.
 */
public enum  Effectstype {

    Fadein(FadeIn.class),
    Slideleft(SlideLeft.class),
    Slidetop(SlideTop.class),
    SlideBottom(SlideBottom.class),
    Slideright(SlideRight.class),
    Fall(Fall.class),
    Newspager(NewsPaper.class),
    Fliph(FlipH.class),
    Flipv(FlipV.class),
    RotateBottom(RotateBottom.class),
    RotateLeft(RotateLeft.class),
    Slit(Slit.class),
    Shake(Shake.class),
    Sidefill(SideFall.class);
    private Class effectsClazz;

    private Effectstype(Class mclass) {
        effectsClazz = mclass;
    }

    public BaseEffects getAnimator() {
        try {
            return (BaseEffects) effectsClazz.newInstance();
        } catch (Exception e) {
            throw new Error("Can not init animatorClazz instance");
        }
    }
}
