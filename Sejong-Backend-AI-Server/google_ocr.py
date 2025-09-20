# google_ocr.py
import os
from google.cloud import vision
import io

# JSON 키 경로를 환경변수로 설정
os.environ['GOOGLE_APPLICATION_CREDENTIALS'] = 'google_credentials.json'

def extract_text_from_image(image_path):
    client = vision.ImageAnnotatorClient()

    with io.open(image_path, 'rb') as image_file:
        content = image_file.read()

    image = vision.Image(content=content)
    response = client.text_detection(image=image)

    if response.error.message:
        raise Exception(f'API Error: {response.error.message}')

    texts = response.text_annotations
    if not texts:
        return None

    return texts[0].description.strip()  # 전체 인식 텍스트
