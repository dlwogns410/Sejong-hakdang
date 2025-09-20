# convert_model.py
import tensorflow as tf
from keras.models import load_model
from keras.layers import Dense

# 1. 모델 로드 (불러오는 시점에서 오류가 날 수도 있음)
model = load_model('model_300.h5', compile=False, custom_objects={'Dense': Dense})

# 2. 호환 가능한 SavedModel 형식으로 저장
model.save('saved_model/')
print("✅ 모델을 SavedModel 형식으로 저장 완료!")
