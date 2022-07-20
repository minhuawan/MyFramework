--
-- Auto generated, do not edit manually
-- date 2022-07-20 13:31:30
--
return {
    attach = function(binder)
        return {
            transforms = {
                title = binder.transforms[0],
                buttonOk = binder.transforms[1],
            },
            gameObjects = {
                title = binder.gameObjects[0],
                buttonOk = binder.gameObjects[1],
            },
            texts = {
                title = binder.texts[0],
            },
            images = {
                buttonOk = binder.images[0],
            },
            buttonViews = {
                buttonOk = binder.buttonViews[0],
            },
        }
    end
}
