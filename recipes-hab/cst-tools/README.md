# CompuLab cst-tools

## How to build

<pre>
bitbake -k cst-tools
</pre>

## Output Layout

<pre>
cst-tools/
├── bin
│   ├── csf.f
│   ├── csf.k
│   └── csf.u
├── ca
│   ├── openssl.cnf
│   ├── v3_ca.cnf
│   └── v3_usr.cnf
├── crts
│   ├── hab4_pki_tree.sh
│   ├── ...
│   ├── ...
├── hab
│   ├── csf_additional_images.in
│   ├── csf_fit.in
│   ├── csf_spl.in
│   ├── flash_evk -> imx-boot-mcm-imx8m-mini-sd.bin-flash_evk
│   ├── flash_evk.log
│   ├── Image
│   ├── imx-boot-mcm-imx8m-mini-sd.bin-flash_evk
│   ├── print_fit_hab.log
│   └── signed
│       ├── f
│       │   └── fuse.out -- fuse values
│       ├── k
│       │   ├── csf_additional_images.in
│       │   ├── genivt
│       │   ├── Image
│       │   ├── Image_csf
│       │   ├── Image_pad
│       │   ├── Image_pad_ivt
│       │   ├── ivt.bin
│       │   └── signed -> Image -- signed kernel
│       └── u
│           ├── csf_fit.bin
│           ├── csf_fit.txt
│           ├── csf_spl.bin
│           ├── csf_spl.txt
│           ├── imx-boot-mcm-imx8m-mini-sd.bin-flash_evk
│           └── signed -> imx-boot-mcm-imx8m-mini-sd.bin-flash_evk -- signed imx-boot image
├── keys -> crts
└── linux64
    └── bin
        └── cst
</pre>

## How to use

### Signing with already generated keys

Goto `hab` input directory:
<pre>
cd ${BUILDIR}/tmp/deploy/images/${MACHINE}/cst-tools/hab
</pre>

* Kernel signing

|Input | Script | Output |
|--- | --- |---|
|Image<br>csf_additional_images.in<br>crts/\*<br>keys/\*<br>bin/csf.in| ../bin/csf.k |signed/k/Image

<pre>
../bin/csf.k
stat signed/k/Image
</pre>

* U-Boot  signing

| Input | Script | Output |
|--- | --- |---|
|imx-boot-mcm-imx8m-mini-sd.bin-flash_evk<br>csf_fit.in<br>csf_spl.in<br>flash_evk.log<br>crts/\*<br>keys/\*<br>bin/csf.in| ../bin/csf.u |signed/u/imx-boot-mcm-imx8m-mini-sd.bin-flash_evk

<pre>
../bin/csf.u
stat signed/u/imx-boot-mcm-imx8m-mini-sd.bin-flash_evk
</pre>

* fuse  signing
<pre>
../bin/csf.f
stat signed/f/fuse.out
</pre>
