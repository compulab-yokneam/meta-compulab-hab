BOOTAA64_EFI ?= "${EFI_PREFIX}/EFI/BOOT/bootaa64.efi"
 
compulab_bootaa64_efi() {
    mv ${IMAGE_ROOTFS}/${BOOTAA64_EFI} ${IMAGE_ROOTFS}/${BOOTAA64_EFI}.unsigned
    mv ${IMAGE_ROOTFS}/${BOOTAA64_EFI}.signed ${IMAGE_ROOTFS}/${BOOTAA64_EFI}
}

compulab_kernel_image() {
    unlink ${IMAGE_ROOTFS}/boot/Image
    ln -sf Image.kgrub.signed ${IMAGE_ROOTFS}/boot/Image
}
