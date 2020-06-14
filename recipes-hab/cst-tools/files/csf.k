#!/bin/bash -ex

SOC=$(pwd)
CST=../linux64/bin/cst

command -v ${CST} &>/dev/null
if [[ $? -ne 0 ]];then
cat << eom
"${CST} - binary is missing, exiting ..."
eom
exit 1
fi

O=${SOC}/signed/k
install -d ${O}

kload=0x40480000
# tload=0x43000000

ifile=Image
ofile=${ifile}

padfile=${O}/${ifile}_pad
ivtfile=${O}/${ifile}_pad_ivt
csffile=${O}/${ifile}_csf
ivtbin=${O}/ivt.bin
i_csfaddfile=csf_additional_images.in
o_csfaddfile=${O}/${i_csfaddfile}
genivt=${O}/genivt

padsize=0x$(xxd -s 0x10 -g 0x4 -l 0x4 -e -u ${ifile} | awk '$0=$2')
objcopy -I binary -O binary --pad-to ${padsize} --gap-fill=0x00 ${ifile} ${padfile}

ivt_off=$(printf 0x%x ${padsize})
ivt_ptr=$(printf 0x%x $((${kload}+${padsize})))
csf_ptr=$(printf 0x%x $((${kload}+${padsize}+0x20)))

cat << eoscript > ${genivt}
#! /usr/bin/perl -w
use strict;
open(my \$out, '>:raw', '${ivtbin}') or die "Unable to open: \$!";
print \$out pack("V", 0x412000D1); # Signature
print \$out pack("V", ${kload}); # Load Address (*load_address)
print \$out pack("V", 0x0);
print \$out pack("V", 0x0);
print \$out pack("V", 0x0);
print \$out pack("V", ${ivt_ptr}); # Self Pointer (*ivt)
print \$out pack("V", ${csf_ptr}); # CSF Pointer (*csf)
print \$out pack("V", 0x0);
close(\$out);
eoscript
chmod a+x ${genivt}
${genivt}

cat ${padfile} ${ivtbin} > ${ivtfile}

ivtsize=$(printf 0x%x $(stat -c %s ${ivtfile}))

BLOCKS="  ${kload} 0x00000000 ${ivtsize} \"${ivtfile}\" "
# As of now the Image file is getting signed only
# BLOCKS+=" , ${tload} 0x00000000 ${tsize} \"${tfile}\" "

(cat ${i_csfaddfile} ; cat <<< ${BLOCKS} ) > ${o_csfaddfile}

${CST} --i ${o_csfaddfile} --o ${csffile}

cat ${ivtfile} ${csffile} > ${O}/${ofile}

ln -sf ${ofile} ${O}/signed

cat << eof >> ${O}/hab_auth_img.cmd
setenv ivt_off ${ivt_off}
load mmc \${mmcdev} \${loadaddr} \${image}
hab_auth_img \${loadaddr} \${filesize} \${ivt_off}
eof

# rm -rf ${ivtfile} ${ivtbin} ${padfile} ${csffile} ${csfaddfile} in_${csfaddfile} ${genivt}
