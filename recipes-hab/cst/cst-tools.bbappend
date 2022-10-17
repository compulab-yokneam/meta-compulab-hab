do_configure:append () {
    sed -i 's/\(Features =\).*/\1 MFG/g' ${DEPLOY_DIR_IMAGE}/cst-tools/hab/csf_spl.in
}
