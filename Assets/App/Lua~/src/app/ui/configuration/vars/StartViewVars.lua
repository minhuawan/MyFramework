--
-- date: 2022-10-08 13:20:08
-- file: Assets/AppData/Prefab/STS/View/StartView.prefab
-- Auto generated, do not edit manually
--
local __return__
local ButtonViewWrap = require('app.ui.base.ButtonViewWrap')
return {
    attach = function(binder)
        __return__ = {
            Transforms = {
                btnBack = binder.Transforms[0],
                menuNormal = binder.Transforms[1],
            },
            GameObjects = {
                btnBack = binder.GameObjects[0],
                menuNormal = binder.GameObjects[1],
            },
            Texts = {
            },
            Images = {
                menuNormal = binder.Images[0],
            },
            ButtonViews = {
                btnBack = ButtonViewWrap(binder.ButtonViews[0]),
                menuNormal = ButtonViewWrap(binder.ButtonViews[1]),
            },
            dispose = function()
                __return__.ButtonViews.btnBack:dispose()
                __return__.ButtonViews.menuNormal:dispose()
            end,
        }
        return __return__
    end,
}
