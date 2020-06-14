all: build

keys/hab4_pki_tree.sh:
	ln -sf ../tools/hab4_pki_tree.sh keys/hab4_pki_tree.sh

crts/SRK1_sha256_2048_65537_v3_ca_crt.pem: tools/gen_keys.sh keys/hab4_pki_tree.sh
	cd keys && ../tools/gen_keys.sh
	
crts/SRK_1_2_3_4_*.bin: crts/SRK1_sha256_2048_65537_v3_ca_crt.pem linux64/bin/srktool tools/gen_srk.sh
	cd crts && ../tools/gen_srk.sh

srk:	crts/SRK_1_2_3_4_*.bin

hab/signed/k hab/signed/u hab/signed/f: crts/SRK_1_2_3_4_*.bin
	cd hab && ../tools/csf.$(@F)

sign_req: srk linux64/bin/cst

kernel_in: hab/Image hab/csf_additional_images.in
kernel:	sign_req kernel_in hab/signed/k

imx-boot_in: hab/flash.bin hab/flash_evk.log hab/print_fit_hab.log hab/csf_fit.in hab/csf_spl.in
imx-boot: sign_req imx-boot_in hab/signed/u

fuse: srk crts/SRK_1_2_3_4_*.bin hab/signed/f

clean:
	rm -rf hab/signed

clean_keys:
	rm -rf crts/* keys/*

clean_all: clean clean_keys

clean_yocto:
	rm -rf ca crts linux64 keys tools hab

build: kernel imx-boot fuse
