--
-- date: 2022-12-13 17:08:09
-- file: Assets/AppData/Scene/main.unity
-- Auto generated, do not edit manually
--
local __return__
local ButtonViewWrap = require('app.ui.base.ButtonViewWrap')
return {
    attach = function(binder)
        __return__ = {
            Transforms = {
                txtEnergy = binder.Transforms[0],
                btnDeck = binder.Transforms[1],
                deckCount = binder.Transforms[2],
                btnDiscard = binder.Transforms[3],
                discardCount = binder.Transforms[4],
                btnOver = binder.Transforms[5],
            },
            GameObjects = {
                txtEnergy = binder.GameObjects[0],
                btnDeck = binder.GameObjects[1],
                deckCount = binder.GameObjects[2],
                btnDiscard = binder.GameObjects[3],
                discardCount = binder.GameObjects[4],
                btnOver = binder.GameObjects[5],
            },
            Texts = {
                txtEnergy = binder.Texts[0],
                deckCount = binder.Texts[1],
                discardCount = binder.Texts[2],
            },
            Images = {
                btnDeck = binder.Images[0],
                btnDiscard = binder.Images[1],
                btnOver = binder.Images[2],
            },
            ButtonViews = {
                btnDeck = ButtonViewWrap(binder.ButtonViews[0]),
                btnDiscard = ButtonViewWrap(binder.ButtonViews[1]),
                btnOver = ButtonViewWrap(binder.ButtonViews[2]),
            },
            dispose = function()
                __return__.ButtonViews.btnDeck:dispose()
                __return__.ButtonViews.btnDiscard:dispose()
                __return__.ButtonViews.btnOver:dispose()
            end,
        }
        return __return__
    end,
}
