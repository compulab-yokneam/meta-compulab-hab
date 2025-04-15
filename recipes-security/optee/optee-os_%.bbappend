EXTRA_OEMAKE:append = "${@bb.utils.contains('COMPULAB_FEATURES', 'optee-debug', 'CFG_TEE_CORE_LOG_LEVEL=4', '', d)}"
