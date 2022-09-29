--
-- date: 2022-09-29 23:34:53
-- file: Assets/AppData/Prefab/STS/View/CharacterSelectView.prefab
-- Auto generated, do not edit manually
--
local __return__
local ButtonViewWrap = require('app.ui.base.ButtonViewWrap')
return {
    attach = function(binder)
        __return__ = {
            Transforms = {
                characterInfo = binder.Transforms[0],
                characterPoster = binder.Transforms[1],
                characterName = binder.Transforms[2],
                hpText = binder.Transforms[3],
                goldText = binder.Transforms[4],
                title = binder.Transforms[5],
                relicsIcon = binder.Transforms[6],
                relicsName = binder.Transforms[7],
                relicsDescription = binder.Transforms[8],
                btnBack = binder.Transforms[9],
                btnStart = binder.Transforms[10],
                buttonIron = binder.Transforms[11],
                buttonSilent = binder.Transforms[12],
            },
            GameObjects = {
                characterInfo = binder.GameObjects[0],
                characterPoster = binder.GameObjects[1],
                characterName = binder.GameObjects[2],
                hpText = binder.GameObjects[3],
                goldText = binder.GameObjects[4],
                title = binder.GameObjects[5],
                relicsIcon = binder.GameObjects[6],
                relicsName = binder.GameObjects[7],
                relicsDescription = binder.GameObjects[8],
                btnBack = binder.GameObjects[9],
                btnStart = binder.GameObjects[10],
                buttonIron = binder.GameObjects[11],
                buttonSilent = binder.GameObjects[12],
            },
            Texts = {
                characterName = binder.Texts[0],
                hpText = binder.Texts[1],
                goldText = binder.Texts[2],
                title = binder.Texts[3],
                relicsName = binder.Texts[4],
                relicsDescription = binder.Texts[5],
            },
            Images = {
                characterPoster = binder.Images[0],
                relicsIcon = binder.Images[1],
            },
            ButtonViews = {
                btnBack = ButtonViewWrap(binder.ButtonViews[0]),
                btnStart = ButtonViewWrap(binder.ButtonViews[1]),
                buttonIron = ButtonViewWrap(binder.ButtonViews[2]),
                buttonSilent = ButtonViewWrap(binder.ButtonViews[3]),
            },
            dispose = function()
                __return__.ButtonViews.btnBack:dispose()
                __return__.ButtonViews.btnStart:dispose()
                __return__.ButtonViews.buttonIron:dispose()
                __return__.ButtonViews.buttonSilent:dispose()
            end,
        }
        return __return__
    end,
}
