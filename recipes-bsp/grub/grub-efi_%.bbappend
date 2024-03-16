FILESEXTRAPATHS:prepend := "${THISDIR}/files:"
SRC_URI += " \
	file://0001-arm64-Set-upper-limit-to-0x55ffffff.patch \
	file://0002-arm64-linux-Use-static-kernel-load-address.patch \
"

GRUB_IMAGE_DEFAULT ?= "bootaa64.efi"
DEST = "${DEPLOY_DIR_IMAGE}/cst-tools/hab"

do_compile_hab () {
    install -d ${DEST}
    install -m 644 ${B}/${GRUB_IMAGE_PREFIX}${GRUB_IMAGE} ${DEST}/${GRUB_IMAGE_DEFAULT}
}
addtask compile_hab
do_compile_hab[nostamp] = "1"
do_compile_hab[depends] = "grub-efi:do_install"
