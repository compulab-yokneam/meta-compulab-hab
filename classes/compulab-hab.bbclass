compulab_bootaa64_efi() {
    BOOTAA64_EFI="boot/EFI/BOOT/bootaa64.efi"
    mv ${IMAGE_ROOTFS}/${BOOTAA64_EFI}.signed ${IMAGE_ROOTFS}/${BOOTAA64_EFI}
}

compulab_kernel_image() {
    ln -sf Image.signed ${IMAGE_ROOTFS}/boot/Image
    image_name=$(ls ${IMAGE_ROOTFS}/boot | awk '/Image-/')
    if [ -n "${image_name}" ];then
        ln -sf Image ${IMAGE_ROOTFS}/boot/${image_name}
    fi
}
