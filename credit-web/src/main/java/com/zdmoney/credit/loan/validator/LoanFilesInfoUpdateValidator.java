package com.zdmoney.credit.loan.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.zdmoney.credit.common.util.ValidatorUtil;
import com.zdmoney.credit.loan.vo.VLoanFilesInfoUpdate;

public class LoanFilesInfoUpdateValidator implements Validator  {

	@Override
	public boolean supports(Class<?> clazz) {
		return VLoanFilesInfoUpdate.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		VLoanFilesInfoUpdate vLoanFilesInfoUpdate = (VLoanFilesInfoUpdate)target;

		//住址证明
		notNullValidator(errors, "住址证明数量", vLoanFilesInfoUpdate.getAddressCertificationCount(),vLoanFilesInfoUpdate.getAddressCertification(),vLoanFilesInfoUpdate.getAddressCertificationOther());

		//资产证明
		notNullValidator(errors, "资产证明数量", vLoanFilesInfoUpdate.getAssetCertificationCount(),vLoanFilesInfoUpdate.getAssetCertification(),vLoanFilesInfoUpdate.getAssetCertificationOther());

		//经营证明
		notNullValidator(errors, "经营证明数量", vLoanFilesInfoUpdate.getBusinessCertificationCount(),vLoanFilesInfoUpdate.getBusinessCertification(),vLoanFilesInfoUpdate.getBusinessCertificationOther());

		//身份证明
		notNullValidator(errors, "身份证明", vLoanFilesInfoUpdate.getIdCertificationCount(),vLoanFilesInfoUpdate.getIdCertification(),vLoanFilesInfoUpdate.getIdCertificationOther());

		//收入证明
		notNullValidator(errors, "收入证明", vLoanFilesInfoUpdate.getIncomeCertificationCount(),vLoanFilesInfoUpdate.getIncomeCertification(),vLoanFilesInfoUpdate.getIncomeCertificationOther());

		//其他证明
		notNullValidator(errors, "其他证明", vLoanFilesInfoUpdate.getOtherCertificationCount(),vLoanFilesInfoUpdate.getOtherCertification(),vLoanFilesInfoUpdate.getOtherCertificationOther());

		//工作证明
		notNullValidator(errors, "工作证明", vLoanFilesInfoUpdate.getWorkCertificationCount(),vLoanFilesInfoUpdate.getWorkCertification(),vLoanFilesInfoUpdate.getWorkCertificationOther());
	}
	
	public static void notNullValidator(Errors errors,String fieldName,Long value,String reference1,String reference2){
		if(value!=null){
			if(value<0){
				errors.reject("Min", fieldName+"必须大于等于0");
			}
		}else{
			if((reference1!=null&&reference1.length()>0) || (reference2!=null && reference2.length()>0)){
				errors.reject("NotNull",fieldName+"不能为空");
			}
		}
	}

}
