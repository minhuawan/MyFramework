--
-- file: Assets/AppData/Prefab/STS/View/MainView.prefab
-- hash: c5de0028abfa56cb84f2b42dac4217d3
-- path: MainView
-- Auto generated, do not edit manually
--
local __return__
local ButtonViewWrap = require('app.ui.base.ButtonViewWrap')
return {
    attach = function(binder)
        __return__ = {
            Transforms = {
                avatar = binder.Transforms[0],
                nickname = binder.Transforms[1],
                btnEdit = binder.Transforms[2],
                btnStart = binder.Transforms[3],
                btnWiki = binder.Transforms[4],
                btnAnalysis = binder.Transforms[5],
                btnSetting = binder.Transforms[6],
                btnPatch = binder.Transforms[7],
                btnExit = binder.Transforms[8],
            },
            GameObjects = {
                avatar = binder.GameObjects[0],
                nickname = binder.GameObjects[1],
                btnEdit = binder.GameObjects[2],
                btnStart = binder.GameObjects[3],
                btnWiki = binder.GameObjects[4],
                btnAnalysis = binder.GameObjects[5],
                btnSetting = binder.GameObjects[6],
                btnPatch = binder.GameObjects[7],
                btnExit = binder.GameObjects[8],
            },
            Texts = {
                nickname = binder.Texts[0],
                btnEdit = binder.Texts[1],
            },
            Images = {
                avatar = binder.Images[0],
                btnStart = binder.Images[1],
                btnWiki = binder.Images[2],
                btnAnalysis = binder.Images[3],
                btnSetting = binder.Images[4],
                btnPatch = binder.Images[5],
                btnExit = binder.Images[6],
            },
            ButtonViews = {
                btnEdit = ButtonViewWrap(binder.ButtonViews[0]),
                btnStart = ButtonViewWrap(binder.ButtonViews[1]),
                btnWiki = ButtonViewWrap(binder.ButtonViews[2]),
                btnAnalysis = ButtonViewWrap(binder.ButtonViews[3]),
                btnSetting = ButtonViewWrap(binder.ButtonViews[4]),
                btnPatch = ButtonViewWrap(binder.ButtonViews[5]),
                btnExit = ButtonViewWrap(binder.ButtonViews[6]),
            },
            dispose = function()
                __return__.ButtonViews.btnEdit:dispose()
                __return__.ButtonViews.btnStart:dispose()
                __return__.ButtonViews.btnWiki:dispose()
                __return__.ButtonViews.btnAnalysis:dispose()
                __return__.ButtonViews.btnSetting:dispose()
                __return__.ButtonViews.btnPatch:dispose()
                __return__.ButtonViews.btnExit:dispose()
            end,
        }
        return __return__
    end,
}
