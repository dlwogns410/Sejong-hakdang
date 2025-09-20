import os
import sys
from ultralytics import YOLO
from PIL import Image

def image_classification(img_path):
    # YOLOv9-C 모델 로드 (처음 실행 시 모델 자동 다운로드)
    model = YOLO("yolov8m.pt")

    # 이미지 예측
    results = model(img_path)

    # 예측 결과 추출
    predictions = results[0].boxes

    if predictions is None or len(predictions) == 0:
        print("❌ 인식된 객체가 없습니다.")
        return "인식된 객체 없음"

    # 가장 확률 높은 클래스 이름 반환
    class_id = int(predictions.cls[0].item())
    class_name = model.names[class_id]

    print(f"✅ 인식된 객체: {class_name}")
    return class_name

# 🔹 하드코딩된 이미지 경로를 사용한 직접 실행용
if __name__ == "__main__":
    # 테스트 이미지 경로 (Windows 경로 주의: 백슬래시는 r""로 처리)
    img_path = r"C:\\Users\\cat\\Desktop\\Ganada-Backend-AI-Server\\pen.jpg"

    if not os.path.exists(img_path):
        print(f"❌ 파일 없음: {img_path}")
        sys.exit(1)

    image_classification(img_path)
