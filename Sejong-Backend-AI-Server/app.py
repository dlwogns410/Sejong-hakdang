import os
from PIL import Image
from flask import Flask, request, jsonify
from werkzeug.utils import secure_filename
import translate
import save_mobileNetV2 as classification
import picture_caption as pc  # Azure 기반 캡셔닝
import io
import time
import re

# Google Cloud Vision
from google.cloud import vision

app = Flask(__name__)
static_dir = 'images/'

# OCR 클라이언트 초기화
os.environ['GOOGLE_APPLICATION_CREDENTIALS'] = 'lofty-shine-443207-r2-2f8f7ec02ada.json'
ocr_client = vision.ImageAnnotatorClient()


@app.route('/gallery-caption', methods=['GET', 'POST'])
def galleryHome():
    if request.method == "GET":
        return {"captions": "GET 요청은 지원되지 않습니다."}

    elif request.method == 'POST':
        file = request.files['file']
        name = 'test_' + file.filename
        file.save(secure_filename(name))

        kind = make_kind(name)
        caption_message = make_picture_caption(name)

        return {
            "type": "object",
            "kind": kind,
            "message": caption_message,
            "word_example": None
        }
    else:
        return jsonify({"captions": "Refresh again!"})


@app.route('/picture-caption', methods=['POST'])
def pictureHome():
    if request.method == 'POST':
        file = request.files['file']
        name = 'test_' + file.filename
        file.save(secure_filename(name))

        kind = make_kind(name)
        caption_message = make_picture_caption(name)

        return {
            "type": "object",
            "kind": kind,
            "message": caption_message,
            "word_example": None
        }
    else:
        return jsonify({"captions": "Refresh again!"})


@app.route('/text-recognition', methods=['POST'])
def text_recognition():
    if request.method == 'POST':
        file = request.files['file']
        name = 'ocr_' + file.filename
        file.save(secure_filename(name))

        extracted_text = extract_text_from_image(name)

        if not extracted_text:
            return {
                "type": "text",
                "kind": None,
                "message": "인식된 텍스트가 없습니다.",
                "word_example": {
                    "word": "없음",
                    "example": "예문 없음"
                }
            }

        words = re.findall(r'\b[가-힣]{2,}\b', extracted_text)  # 한글 단어 추출 (2글자 이상)
        if not words:
            return {
                "type": "text",
                "kind": None,
                "message": "한글 단어가 없습니다.",
                "word_example": {
                    "word": "없음",
                    "example": "예문 없음"
                }
            }

        word = words[0]
        example = translate.generate_korean_example(word)

        return {
            "type": "text",
            "kind": None,
            "message": f"'{word}'에 대한 예문입니다.",
            "word_example": {
                "word": word,
                "example": example
            }
        }


@app.route('/test-api', methods=['GET', 'POST'])
def testHome():
    return jsonify({"message": "테스트 성공!"})


def make_kind(filename):
    kind = classification.image_classification(filename)
    kind = translate.translate_en_to_ko(kind)
    return kind


def make_picture_caption(filename):
    captions = pc.make_caption(filename)
    if captions is not None:
        translated = translate.translate_en_to_ko(captions)
        return translated
    else:
        return None


def extract_text_from_image(filename):
    """
    Google Cloud Vision OCR을 사용하여 이미지에서 텍스트 추출
    """
    with io.open(filename, 'rb') as image_file:
        content = image_file.read()
    image = vision.Image(content=content)

    response = ocr_client.text_detection(image=image)
    texts = response.text_annotations

    if texts:
        return texts[0].description.strip()
    else:
        return None


if __name__ == '__main__':
    app.run(debug=True, host='0.0.0.0', port=8000)
