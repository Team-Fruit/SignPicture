var ASMAPI = Java.type('net.minecraftforge.coremod.api.ASMAPI');

function modType(modId, className) {
    try {
        return this.engine.factory.scriptEngine.compile('Java.type(\"' + className + '\");').eval();
    } catch (err) {
        var script = " \n\
        function getPrivateValue(instance, name) { \n\
            var f = instance.getClass().getDeclaredField(name); \n\
            f.setAccessible(true); \n\
            return f.get(instance); \n\
        } \n\
        var transforms; \n\
        var FMLLoader = Java.type('net.minecraftforge.fml.loading.FMLLoader'); \n\
        var Class = Java.type('java.lang.Class'); \n\
        var URLClassLoader = Java.type('java.net.URLClassLoader'); \n\
        var engine = getPrivateValue(FMLLoader.getCoreModProvider(), 'engine'); \n\
        var coreMods = getPrivateValue(engine, 'coreMods'); \n\
        var coreModFile = coreMods.stream().map(function(e) { return e.getFile(); }).filter(function(e) { return e.getOwnerId() === '" + modId + "' }).findFirst(); \n\
        if (!coreModFile.isPresent()) \n\
            throw new Error('Failed to load Java transformer from JavaScript transformer: Missing mod'); \n\
        var coreMod = getPrivateValue(coreModFile.get(), 'file'); \n\
        var classLoader = new URLClassLoader([ coreMod.getFilePath().toUri().toURL() ], coreMod.getClass().getClassLoader()); \n\
        Class.forName('" + className + "', true, classLoader).static; \n\
        ";
        return this.engine.factory.scriptEngine.compile(script).eval();
    }
}

function initializeCoreMod() {
    var transformers = {};
    ASMAPI.log('INFO', 'EmojicordTransformer initialized');
    var Transforms = modType('emojicord', 'net.teamfruit.emojicord.asm.EmojicordTransforms');
    Array.prototype.forEach.call(Transforms.transformers,function(transform) {
        var simpleName = Transforms.getSimpleClassName(transform);
        var className = transform.getClassName().getName();
        transformers[simpleName] = {
            'target': {
                'type': 'CLASS',
                'name': className
            },
            'transformer': function (node) {
                ASMAPI.log('INFO', 'Patching ' + className + ' (class: ' + node.name + ')');
                node = transform.apply(node)
                ASMAPI.log('INFO', 'Finished Patching ' + className + ' (class: ' + node.name + ')');
                return node;
            }
        };
    });
    return transformers;
}