GRUB_IMAGE_DEFAULT ?= "bootaa64.efi"
DEST = "${DEPLOY_DIR_IMAGE}/cst-tools/hab"

do_compile_hab () {
    install -d ${DEST}
    install -m 644 ${B}/${GRUB_IMAGE_PREFIX}${GRUB_IMAGE} ${DEST}/${GRUB_IMAGE_DEFAULT}
}
addtask compile_hab
do_compile_hab[nostamp] = "1"
do_compile_hab[depends] = "grub-efi:do_install"
