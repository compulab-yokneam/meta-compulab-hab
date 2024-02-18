LICENSE = "CLOSED"
LIC_FILES_CHKSUM = ""

SRC_URI = "git://github.com/compulab-yokneam/cst-tools.git;protocol=https;branch=master"

PV = "1.1"
SRCREV = "5e2f9f64dfaeae3a0e37652b15eda72a08f2969d"

DEPENDS = "openssl-native imx-boot linux-compulab"

S = "${WORKDIR}/git"

do_configure () {
    SOC=imx8 DEST=${DEPLOY_DIR_IMAGE} SRC=${S} ${S}/bootstrap.sh
}

do_compile_fuse () {
    cd ${DEPLOY_DIR_IMAGE}/cst-tools
    oe_runmake fuse
}

do_compile_kernel () {
    cd ${DEPLOY_DIR_IMAGE}/cst-tools
    oe_runmake -j 1 kernel
}

do_compile_bootloader () {
    cd ${DEPLOY_DIR_IMAGE}/cst-tools
    oe_runmake imx-boot
}

do_compile_uefi () {
    cd ${DEPLOY_DIR_IMAGE}/cst-tools
    oe_runmake uefi
}

do_compile () {
    do_compile_fuse
    do_compile_kernel
    do_compile_bootloader
    do_compile_uefi
}
do_compile[depends] += "imx-boot:do_compile_hab"
do_compile[depends] += "linux-compulab:do_compile_hab"
do_compile[depends] += "grub-efi:do_compile_hab"

do_deploy() {
    cp ${DEPLOY_DIR_IMAGE}/cst-tools/hab/signed/k/Image ${DEPLOY_DIR_IMAGE}/Image.signed
    cp ${DEPLOY_DIR_IMAGE}/cst-tools/hab/signed/kgrub/Image ${DEPLOY_DIR_IMAGE}/Image.kgrub.signed
    cp ${DEPLOY_DIR_IMAGE}/cst-tools/hab/signed/u/flash.bin ${DEPLOY_DIR_IMAGE}/flash.bin.signed
    cp ${DEPLOY_DIR_IMAGE}/cst-tools/hab/signed/f/fuse.out ${DEPLOY_DIR_IMAGE}/fuse.out
    cp ${DEPLOY_DIR_IMAGE}/cst-tools/hab/signed/uefi/bootaa64.efi ${DEPLOY_DIR_IMAGE}/bootaa64.efi.signed
}

do_deploy:append() {
    # Rename the signed binaries to their real name.
    mv ${DEPLOY_DIR_IMAGE}/Image.signed ${DEPLOY_DIR_IMAGE}/Image
    mv ${DEPLOY_DIR_IMAGE}/bootaa64.efi.signed ${DEPLOY_DIR_IMAGE}/bootaa64.efi
}

addtask deploy before do_install after do_compile

do_cleanup() {
    rm -rf ${DEPLOY_DIR_IMAGE}/cst-tools
}
addtask cleanup
do_cleanup[nostamp] = "1"
do_cleanall[depends] += "${PN}:do_cleanup"

do_install () {
    install -d ${D}/boot/efi/EFI/BOOT/
    install -m 0644 ${DEPLOY_DIR_IMAGE}/cst-tools/hab/signed/f/fuse.out ${D}/boot/fuse.out
    install -m 0644 ${DEPLOY_DIR_IMAGE}/cst-tools/hab/signed/k/hab_auth_img.cmd ${D}/boot/hab_auth_img.cmd
    install -m 0644 ${DEPLOY_DIR_IMAGE}/cst-tools/hab/signed/k/Image ${D}/boot/Image.signed
    install -m 0644 ${DEPLOY_DIR_IMAGE}/cst-tools/hab/signed/kgrub/Image ${D}/boot/Image.kgrub.signed
    install -m 0644 ${DEPLOY_DIR_IMAGE}/cst-tools/hab/signed/u/flash.bin ${D}/boot/flash.bin.signed
    install -m 0644 ${DEPLOY_DIR_IMAGE}/cst-tools/hab/signed/uefi/bootaa64.efi ${D}/boot/efi/EFI/BOOT/bootaa64.efi.signed

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
