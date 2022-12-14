--
-- date: 2022-12-13 17:11:10
-- file: Assets/AppData/Scene/main.unity
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
            },
            Texts = {
                userName = binder.Texts[0],
                characterName = binder.Texts[1],
                hpText = binder.Texts[2],
                goldText = binder.Texts[3],
                floorText = binder.Texts[4],
                ascensionText = binder.Texts[5],
                deckText = binder.Texts[6],
            },
            Images = {
                potion1 = binder.Images[0],
                potion2 = binder.Images[1],
                potion3 = binder.Images[2],
                setting = binder.Images[3],
                deck = binder.Images[4],
                map = binder.Images[5],
            },
            ButtonViews = {
                setting = ButtonViewWrap(binder.ButtonViews[0]),
                deck = ButtonViewWrap(binder.ButtonViews[1]),
                map = ButtonViewWrap(binder.ButtonViews[2]),
            },
            dispose = function()
                __return__.ButtonViews.setting:dispose()
                __return__.ButtonViews.deck:dispose()
                __return__.ButtonViews.map:dispose()
            end,
        }
        return __return__
    end,
}
