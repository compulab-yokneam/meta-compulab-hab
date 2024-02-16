compulab_bootaa64_efi() {
    BOOTAA64_EFI="boot/efi/EFI/BOOT/bootaa64.efi"
    mv ${IMAGE_ROOTFS}/${BOOTAA64_EFI} ${IMAGE_ROOTFS}/${BOOTAA64_EFI}.unsigned
    mv ${IMAGE_ROOTFS}/${BOOTAA64_EFI}.signed ${IMAGE_ROOTFS}/${BOOTAA64_EFI}
}
