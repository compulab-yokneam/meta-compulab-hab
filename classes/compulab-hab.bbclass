compulab_bootaa64_efi() {
    if ${@bb.utils.contains('DISTRO_FEATURES', 'compulab-mender', 'true', 'false', d)};then
        BOOTAA64_EFI_LOCATION="boot/efi/EFI/BOOT/"
    else
        BOOTAA64_EFI_LOCATION="boot/EFI/BOOT/"
    fi
    install -d ${IMAGE_ROOTFS}/${BOOTAA64_EFI_LOCATION}/
    cp ${DEPLOY_DIR_IMAGE}/cst-tools/hab/signed/uefi/bootaa64.efi ${IMAGE_ROOTFS}/${BOOTAA64_EFI_LOCATION}/
}

compulab_kernel_image() {
    cp ${DEPLOY_DIR_IMAGE}/cst-tools/hab/signed/k/Image ${IMAGE_ROOTFS}/boot/Image
    cp ${DEPLOY_DIR_IMAGE}/cst-tools/hab/signed/k/Image ${DEPLOY_DIR_IMAGE}/Image
}
