from azure.cognitiveservices.vision.computervision import ComputerVisionClient
from msrest.authentication import CognitiveServicesCredentials

import os
from PIL import Image
import io
import logging
import configparser as parser

def make_caption(filename):
    # Key & Endpoint    
    properties = parser.ConfigParser()
    properties.read('./config.ini', encoding='utf-8')
    
    subscription_key = properties['AZURE']['subscription_key']
    endpoint = properties['AZURE']['endpoint']

    # 컴퓨터 비전 클라이언트 생성
    computervision_client = ComputerVisionClient(
        endpoint, CognitiveServicesCredentials(subscription_key)
    )

    # ✅ 이미지 열고 리사이징 (1024x1024 이하로 제한)
    image = Image.open(filename)
    image.thumbnail((1024, 1024))  # 너무 크면 Azure가 거부

    # ✅ 메모리 버퍼에 JPEG로 저장
    img_byte_arr = io.BytesIO()
    image.save(img_byte_arr, format='JPEG')
    img_byte_arr.seek(0)

    # ✅ Azure API 요청
    try:
        description_result = computervision_client.describe_image_in_stream(img_byte_arr)
    except Exception as e:
        logging.error(f"Azure Vision API Error: {e}")
        return None

    # ✅ 캡션 결과 반환
    if not description_result.captions:
        logging.debug("No description detected.")
        return None
    else:
        best_caption = description_result.captions[0]
        logging.debug(f"'{best_caption.text}' with confidence {best_caption.confidence:.2%}")
        return best_caption.text
