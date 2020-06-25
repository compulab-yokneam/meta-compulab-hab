LICENSE = "CLOSED"
LIC_FILES_CHKSUM = ""

SRC_URI = "git://github.com/compulab-yokneam/cst-tools.git;protocol=https"

PV = "1.0"
SRCREV = "master"

DEPENDS = "openssl-native u-boot-compulab linux-compulab"

S = "${WORKDIR}/git"

do_configure () {
    SOC=imx7 DEST=${DEPLOY_DIR_IMAGE} SRC=${S} ${S}/bootstrap.sh
}

do_compile () {
    cd ${DEPLOY_DIR_IMAGE}/cst-tools
    oe_runmake fuse
    oe_runmake kernel
    oe_runmake u-boot
}

do_deploy() {
    cp ${DEPLOY_DIR_IMAGE}/cst-tools/hab/signed/k/zImage ${DEPLOY_DIR_IMAGE}/zImage.signed
    cp ${DEPLOY_DIR_IMAGE}/cst-tools/hab/signed/u/u-boot.imx ${DEPLOY_DIR_IMAGE}/u-boot.imx.signed
    cp ${DEPLOY_DIR_IMAGE}/cst-tools/hab/signed/f/fuse.out ${DEPLOY_DIR_IMAGE}/fuse.out
}

addtask deploy before do_install after do_compile

do_install () {
    install -d ${D}/boot/
    install -m 0644 ${DEPLOY_DIR_IMAGE}/cst-tools/hab/signed/f/fuse.out ${D}/boot/fuse.out
    install -m 0644 ${DEPLOY_DIR_IMAGE}/cst-tools/hab/signed/k/zImage ${D}/boot/zImage.signed
    install -m 0644 ${DEPLOY_DIR_IMAGE}/cst-tools/hab/signed/u/u-boot.imx ${D}/boot/u-boot.imx.signed

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

FILES_${PN} = " \
    /boot/* \
    /opt/* \
"

PACKAGE_ARCH = "${MACHINE_ARCH}"
