LICENSE = "BSD-3-Clause & LGPL-2.1-only"
LIC_FILES_CHKSUM = "file://licenses/LICENSE.bsd3;md5=1ef4297097d818a9787ed775218c133f \
                    file://licenses/LICENSE.hidapi;md5=7c3949a631240cb6c31c50f3eb696077 \
                    file://licenses/LICENSE.json-c;md5=de54b60fbbc35123ba193fea8ee216f2 \
                    file://licenses/LICENSE.liboqs;md5=4b93ef2da47496727a4e8a59f443844e \
                    file://licenses/LICENSE.libp11;md5=fad9b3332be894bab9bc501572864b29 \
                    file://licenses/LICENSE.libusb;md5=fbc093901857fcd118f065f900982c24 \
                    file://licenses/LICENSE.openssl;md5=3441526b1df5cc01d812c7dfc218cea6 \
                    file://licenses/LICENSE.oqsprovider;md5=ab9b4308908ace39992d3080dd26824a"

SRC_URI = "git://github.com/compulab-yokneam/cst-tools.git;protocol=https;branch=cst-4.0.0-devel"

PV = "1.4"
SRCREV = "${AUTOREV}"

DEPENDS = "openssl-native imx-boot linux-compulab dtc-native"

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

do_compile_init () {
    cd ${DEPLOY_DIR_IMAGE}/cst-tools
    oe_runmake clean
}

do_compile () {
    do_compile_init
    do_compile_fuse
    do_compile_kernel
    do_compile_bootloader
    do_compile_uefi
}
do_compile[depends] += "imx-boot:do_compile_hab"
do_compile[depends] += "u-boot:do_compile_hab"
do_compile[depends] += "linux-compulab:do_compile_hab"
do_compile[depends] += "grub-efi:do_compile_hab"

do_deploy() {
    cp ${DEPLOY_DIR_IMAGE}/cst-tools/hab/signed/k/Image ${DEPLOY_DIR_IMAGE}/Image.signed
    cp ${DEPLOY_DIR_IMAGE}/cst-tools/hab/signed/u/flash.bin ${DEPLOY_DIR_IMAGE}/flash.bin.signed
    cp ${DEPLOY_DIR_IMAGE}/cst-tools/hab/signed/f/fuse.out ${DEPLOY_DIR_IMAGE}/fuse.out
}

do_deploy:append() {
    if ${@bb.utils.contains('DISTRO_FEATURES', 'compulab-uefi', 'true', 'false', d)};then
        cp ${DEPLOY_DIR_IMAGE}/cst-tools/hab/signed/uefi/bootaa64.efi ${DEPLOY_DIR_IMAGE}/bootaa64.efi.signed
    fi
}

addtask deploy before do_install after do_compile

do_cleanup_signed() {
    rm -rf ${DEPLOY_DIR_IMAGE}/cst-tools/hab/signed
}
addtask cleanup_signed
do_cleanup_signed[nostamp] = "1"
do_clean[depends] += "${PN}:do_cleanup_signed"

do_cleanup() {
    rm -rf ${DEPLOY_DIR_IMAGE}/cst-tools
}
addtask cleanup
do_cleanup[nostamp] = "1"
do_cleanall[depends] += "${PN}:do_cleanup"

do_install () {
    install -d ${D}/boot/
    for ff in f/fuse.out k/hab_auth_img.cmd u/flash.bin;do
            f=$(basename ${ff})
            install -m 0644 ${DEPLOY_DIR_IMAGE}/cst-tools/hab/signed/${ff} ${D}/boot/${f}
    done
    mv ${D}/boot/flash.bin ${D}/boot/flash.bin.signed

    install -d ${D}/opt/cst/boot/
    for ff in k/Image uefi/bootaa64.efi;do
            f=$(basename ${ff})
            install -m 0644 ${DEPLOY_DIR_IMAGE}/cst-tools/hab/signed/${ff} ${D}/opt/cst/boot/${f}
    done

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
