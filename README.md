# meta-compulab-hab

This is a meta-layer that allows using the [cst-tools](https://github.com/compulab-yokneam/cst-tools/tree/master/imx7) inside the Yocto.

Supported machines:
* cl-som-imx7

# How to use

## Add to the Yocto build
* Clone the layer repository to the ${BUILDIR}/../sources
```
git clone -b imx7 https://github.com/compulab-yokneam/meta-compulab-hab.git ../sources/meta-compulab-hab
```

* Update the conf/bblayrs.conf
```
cat << eof >> conf/bblayrs.conf
BBLAYERS += "\${BSPDIR}/sources/meta-compulab-hab"
eof
```

* Build the `cst-tools`
```
bitbake -k cst-tools
```

## Yocto build results:
|Target|Location|
|---|---|
|signed u-boot|${BUILDIR}/tmp/deploy/images/${MACHINE}/u-boot.imx.signed|
|signed kernel image|${BUILDIR}/tmp/deploy/images/${MACHINE}/zImage.signed|
|fuse values file|${BUILDIR}/tmp/deploy/images/${MACHINE}/fuse.out|


## Signing procedure with predefine keys

|Note|The `crts` and the `keys` folders contain keys generated by the latest Yocto build.<br>These forders can be replaced with the user generated keys.<br>The `cst-tools` recipe makes use of predefined keys if them are at `crts` and `keys`.|
|---|---|

### Build the image with the user predefined keys

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

* Kernel signing
```
make kernel
stat hab/signed/k/zImage
```

* u-boot signing
```
make u-boot
stat hab/signed/u/u-boot.imx
```

* fuse values generating
```
make fuse
stat hab/signed/f/fuse.out
```

* Generate all targets `kernel`, `u-boot`, `fuse` at the same time:
```
make
```

* Clean up

|Description|Command|
|---|---|
| signed files only |make clean|
| keys only |make clean_keys|
| all files |make clean_all|
