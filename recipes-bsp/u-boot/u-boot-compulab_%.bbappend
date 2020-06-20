FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += "\
    file://0001-cl-som-imx7-Add-u-boot.imx-target.patch \
"

DEST = "${DEPLOY_DIR_IMAGE}/cst-tools/hab"

do_compile_append () {
    install -d ${DEST}
    for target in u-boot-ivt.img u-boot-ivt.img.log SPL SPL.log;do
        cp ${B}/${target} ${DEST}/
    done

    cd ${DEST}/..
    oe_runmake u-boot
    cp ${DEST}/signed/u/u-boot.imx ${B}/
}

DEPENDS += "cst-keys"
COMPATIBLE_MACHINE = "cl-som-imx7"
