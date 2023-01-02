package com.chysk5.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.chysk5.domain.ProductDTO;
import com.chysk5.domain.ProductImgDTO;
import com.chysk5.domain.ProductSizeDTO;
import com.chysk5.mapper.ProductMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;

@Service
@Log4j
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

	private final ProductMapper mapper;

	// 상품 목록 select 메소드
	@Override
	public List<ProductDTO> getPListDB(String category) {
		log.info("call productService...........");

		log.info(category);

		List<ProductDTO> list = mapper.getPList(category);

		log.info(list);

		return mapper.getPList(category);

	}

	@Override
	public ProductDTO getProductOption(String pro_id) {
		log.info("call ProductOptionService..........");
		log.info(pro_id);

		return mapper.getProductOption(pro_id);

	}

	@Override
	public List<ProductSizeDTO> getProductSize(String pro_id) {
		log.info("call ProductSizeService..............");
		log.info(pro_id);

		return mapper.getProductSize(pro_id);
	}
	
	@Override
	public List<ProductImgDTO> getProductImg(String pro_id) {
		log.info("call ProductImgService..............");
		log.info(pro_id);
		
		return mapper.getProductImg(pro_id);
	}

}