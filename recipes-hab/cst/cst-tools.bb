LICENSE = "CLOSED"
LIC_FILES_CHKSUM = ""

SRC_URI = "git://github.com/compulab-yokneam/cst-tools.git;protocol=https;branch=master"

PV = "1.0"
SRCREV = "73f6d84cfbccb4e95d404967bddbfca73a7ad18a"

DEPENDS = "openssl-native imx-boot linux-compulab"

S = "${WORKDIR}/git"

do_configure () {
    SOC=imx8 DEST=${DEPLOY_DIR_IMAGE} SRC=${S} ${S}/bootstrap.sh
}

do_compile () {
    cd ${DEPLOY_DIR_IMAGE}/cst-tools
    oe_runmake fuse
    oe_runmake kernel
    oe_runmake imx-boot
}

do_deploy() {
    cp ${DEPLOY_DIR_IMAGE}/cst-tools/hab/signed/k/Image ${DEPLOY_DIR_IMAGE}/Image.signed
    cp ${DEPLOY_DIR_IMAGE}/cst-tools/hab/signed/u/flash.bin ${DEPLOY_DIR_IMAGE}/flash.bin.signed
    cp ${DEPLOY_DIR_IMAGE}/cst-tools/hab/signed/f/fuse.out ${DEPLOY_DIR_IMAGE}/fuse.out
}

addtask deploy before do_install after do_compile

do_install () {
    install -d ${D}/boot/
    install -m 0644 ${DEPLOY_DIR_IMAGE}/cst-tools/hab/signed/f/fuse.out ${D}/boot/fuse.out
    install -m 0644 ${DEPLOY_DIR_IMAGE}/cst-tools/hab/signed/k/hab_auth_img.cmd ${D}/boot/hab_auth_img.cmd
    install -m 0644 ${DEPLOY_DIR_IMAGE}/cst-tools/hab/signed/k/Image ${D}/boot/Image.signed
    install -m 0644 ${DEPLOY_DIR_IMAGE}/cst-tools/hab/signed/u/flash.bin ${D}/boot/flash.bin.signed

    for d in keys crts;do
        install -d ${D}/opt/cst/${d}/
        for ff in ${DEPLOY_DIR_IMAGE}/cst-tools/${d}/*;do
            f=$(basename ${ff})
            install -m 0644 ${DEPLOY_DIR_IMAGE}/cst-tools/${d}/${f} ${D}/opt/cst/${d}/${f}
        done
        for ext in h bat exe sh old attr txt;do
            rm -rf ${D}/opt/cst/${d}/*.${ext}
        done
    done
}

PROVIDES = "cst-tools"

FILES:${PN} = " \
    /boot/* \
    /opt/* \
"

PACKAGE_ARCH = "${MACHINE_ARCH}"
