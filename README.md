# meta-compulab-hab

This meta-layer allows using the [cst-tools](https://github.com/compulab-yokneam/cst-tools/tree/master/imx8) inside Yocto.

Supported machines:
* ucm-imx8m-mini
* mcm-imx8m-mini
* iot-gate-imx8

# How to use

## Add to the Yocto build
* Clone the layer repository to the ${BUILDIR}/../sources
```
git clone -b imx8 https://github.com/compulab-yokneam/meta-compulab-hab.git ../sources/meta-compulab-hab
```

* Update `conf/bblayers.conf`
```
cat << eof >> conf/bblayers.conf
BBLAYERS += "\${BSPDIR}/sources/meta-compulab-hab"
eof
```

* Build `cst-tools`
```
bitbake -k cst-tools
```

## Yocto build results:
|Target|Location|
|---|---|
|signed imx-boot|${BUILDIR}/tmp/deploy/images/${MACHINE}/flash.bin.signed|
|signed kernel image|${BUILDIR}/tmp/deploy/images/${MACHINE}/Image.signed|
|fuse values file|${BUILDIR}/tmp/deploy/images/${MACHINE}/fuse.out|


## Signing procedure with predefined keys

|Note|The `crts` and the `keys` folders contain keys generated by the latest Yocto build.<br>These forders can be replaced with the user generated keys.<br>The `cst-tools` recipe makes use of predefined keys if them are at `crts` and `keys`.|
|---|---|

### Building the image with predefined keys

* Copy the user predefined `crts` and the `keys` to `${BUILDIR}/tmp/deploy/images/${MACHINE}/cst-tools` folder:
```
cp -a /path/to/user-predefined/{crts,keys} ${BUILDIR}/tmp/deploy/images/${MACHINE}/cst-tools/
```

* Goto to the ${BUILDIR} directory and rebuild the image:
```
bitbake -k cst-tools -c cleansstate
bitbake -k <image>
```

## Using as a standalone tool

* Goto `cst-tools`:
```
cd ${BUILDIR}/tmp/deploy/images/${MACHINE}/cst-tools
```

* Signing the kernel
```
make kernel
stat hab/signed/k/Image
```

* Signing imx-boot
```
make imx-boot
stat hab/signed/u/flash.bin
```

* Generating fuse values
```
make fuse
stat hab/signed/f/fuse.out
```

* Generating all targets `kernel`, `imx-boot`, `fuse` at the same time:
```
make
```

* Cleaning up

|Description|Command|
|---|---|
| signed files only |make clean|
| keys only |make clean_keys|
| all files |make clean_all|

### Fuse Programming
* Fuse progrmming is demonstrated in NXP document [i.MX8M family Secure Boot guide using HABv4](https://github.com/nxp-imx/uboot-imx/blob/lf_v2022.04/doc/imx/habv4/guides/mx8m_secure_boot.txt).
