--
-- date: 2022-09-29 23:35:06
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
                menuNormal2 = binder.Transforms[2],
                menuNormal3 = binder.Transforms[3],
            },
            GameObjects = {
                btnBack = binder.GameObjects[0],
                menuNormal = binder.GameObjects[1],
                menuNormal2 = binder.GameObjects[2],
                menuNormal3 = binder.GameObjects[3],
            },
            Texts = {
            },
            Images = {
                menuNormal = binder.Images[0],
                menuNormal2 = binder.Images[1],
                menuNormal3 = binder.Images[2],
            },
            ButtonViews = {
                btnBack = ButtonViewWrap(binder.ButtonViews[0]),
                menuNormal = ButtonViewWrap(binder.ButtonViews[1]),
                menuNormal2 = ButtonViewWrap(binder.ButtonViews[2]),
                menuNormal3 = ButtonViewWrap(binder.ButtonViews[3]),
            },
            dispose = function()
                __return__.ButtonViews.btnBack:dispose()
                __return__.ButtonViews.menuNormal:dispose()
                __return__.ButtonViews.menuNormal2:dispose()
                __return__.ButtonViews.menuNormal3:dispose()
            end,
        }
        return __return__
    end,
}
