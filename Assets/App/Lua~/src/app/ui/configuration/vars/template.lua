--
-- date: 2022-12-22 16:45:57
-- file: Assets/AppData/Prefab/STS/View/BattleView.prefab
-- Auto generated, do not edit manually
--
local __return__
local ButtonViewWrap = require('app.ui.base.ButtonViewWrap')
return {
    attach = function(binder)
        __return__ = {
            Transforms = {
                userName = binder.Transforms[0],
                characterName = binder.Transforms[1],
                hpText = binder.Transforms[2],
                goldText = binder.Transforms[3],
                potion1 = binder.Transforms[4],
                potion2 = binder.Transforms[5],
                potion3 = binder.Transforms[6],
                floorText = binder.Transforms[7],
                ascensionText = binder.Transforms[8],
                setting = binder.Transforms[9],
                deck = binder.Transforms[10],
                deckText = binder.Transforms[11],
                map = binder.Transforms[12],
                txtEnergy = binder.Transforms[13],
                btnDeck = binder.Transforms[14],
                deckCount = binder.Transforms[15],
                btnDiscard = binder.Transforms[16],
                discardCount = binder.Transforms[17],
                btnOver = binder.Transforms[18],
            },
            GameObjects = {
                userName = binder.GameObjects[0],
                characterName = binder.GameObjects[1],
                hpText = binder.GameObjects[2],
                goldText = binder.GameObjects[3],
                potion1 = binder.GameObjects[4],
                potion2 = binder.GameObjects[5],
                potion3 = binder.GameObjects[6],
                floorText = binder.GameObjects[7],
                ascensionText = binder.GameObjects[8],
                setting = binder.GameObjects[9],
                deck = binder.GameObjects[10],
                deckText = binder.GameObjects[11],
                map = binder.GameObjects[12],
                txtEnergy = binder.GameObjects[13],
                btnDeck = binder.GameObjects[14],
                deckCount = binder.GameObjects[15],
                btnDiscard = binder.GameObjects[16],
                discardCount = binder.GameObjects[17],
                btnOver = binder.GameObjects[18],
            },
            Texts = {
                userName = binder.Texts[0],
                characterName = binder.Texts[1],
                hpText = binder.Texts[2],
                goldText = binder.Texts[3],
                floorText = binder.Texts[4],
                ascensionText = binder.Texts[5],
                deckText = binder.Texts[6],
                txtEnergy = binder.Texts[7],
                deckCount = binder.Texts[8],
                discardCount = binder.Texts[9],
            },
            Images = {
                potion1 = binder.Images[0],
                potion2 = binder.Images[1],
                potion3 = binder.Images[2],
                setting = binder.Images[3],
                deck = binder.Images[4],
                map = binder.Images[5],
                btnDeck = binder.Images[6],
                btnDiscard = binder.Images[7],
                btnOver = binder.Images[8],
            },
            ButtonViews = {
                setting = ButtonViewWrap(binder.ButtonViews[0]),
                deck = ButtonViewWrap(binder.ButtonViews[1]),
                map = ButtonViewWrap(binder.ButtonViews[2]),
                btnDeck = ButtonViewWrap(binder.ButtonViews[3]),
                btnDiscard = ButtonViewWrap(binder.ButtonViews[4]),
                btnOver = ButtonViewWrap(binder.ButtonViews[5]),
            },
            dispose = function()
                __return__.ButtonViews.setting:dispose()
                __return__.ButtonViews.deck:dispose()
                __return__.ButtonViews.map:dispose()
                __return__.ButtonViews.btnDeck:dispose()
                __return__.ButtonViews.btnDiscard:dispose()
                __return__.ButtonViews.btnOver:dispose()
            end,
        }
        return __return__
    end,
}
