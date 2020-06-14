LICENSE = "Unknown"
LIC_FILES_CHKSUM = "file://README;md5=80b3d8b4527bf06c08c3976863f9cc6d"

SRC_URI = "file://README"

DEPENDS = "imx-boot linux-imx"

S = "${WORKDIR}"

do_configure () {
    :
}

do_compile () {
    :
}

do_deploy() {
    cp ${DEPLOY_DIR_IMAGE}/cst-tools/hab/signed/k/Image ${DEPLOY_DIR_IMAGE}/Image.signed
}

do_install () {
    install -d ${D}/boot/
    install -m 0644 ${DEPLOY_DIR_IMAGE}/cst-tools/hab/signed/f/fuse.out ${D}/boot/fuse.out
    install -m 0644 ${DEPLOY_DIR_IMAGE}/cst-tools/hab/signed/k/hab_auth_img.cmd ${D}/boot/hab_auth_img.cmd
    install -m 0644 ${DEPLOY_DIR_IMAGE}/cst-tools/hab/signed/k/Image ${D}/boot/Image.signed

    for d in keys crts;do
        install -d ${D}/opt/cst/${d}/
        for ff in ${DEPLOY_DIR_IMAGE}/cst-tools/${d}/*;do
            f=$(basename ${ff})
            install -m 0644 ${DEPLOY_DIR_IMAGE}/cst-tools/${d}/${f} ${D}/opt/cst/${d}/${f}
        done
        for ext in h bat ext;do
            rm -rf ${D}/opt/cst/${d}/*.${ext}
        done
    done
}

addtask deploy before do_install after do_compile

PROVIDES += "cst-tools"

FILES_${PN} = " \
    /boot/* \
    /opt/* \
"

PACKAGE_ARCH = "${MACHINE_ARCH}"
