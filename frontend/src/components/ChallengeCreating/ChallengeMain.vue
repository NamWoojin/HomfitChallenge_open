<template>
  <form>
    <!-- 챌린지 명, 챌린지 이미지, 챌린지소개 -->
    <div>
      <h4 class="challenge-creating-title">챌린지 정보</h4>
      <b-container>
        <b-row class="challenge-creating-row">
          <b-col sm="3" class="align-center">
            <span class="require-icon">*</span>
            <span>챌린지 이름</span>
            
          </b-col>
          <b-col sm="9">
            <b-form-input
              id="challenge_title_input"
              type="text"
              :state="challengeTitleState"
              placeholder="챌린지 이름을 입력하세요(1~20자)"
              v-model="challenge_title"
            ></b-form-input>
            <b-form-invalid-feedback id="challenge_title_input">
              챌린지 이름을 20자 이내로 입력하세요.
            </b-form-invalid-feedback>
          </b-col>
        </b-row>

        <b-row class="challenge-creating-row">
          <b-col sm="3" class="align-center">
             <span class="require-icon">*</span>
            <span>챌린지 설명</span>
          </b-col>
          <b-col sm="9">
            <TextEditor
              :props_content="challenge_contents"
              @input="(data) => GetEditorContent(data)"
            ></TextEditor>
          </b-col>
        </b-row>

        <b-row class="challenge-creating-row">
          <b-col sm="3" class="align-center ">
            <span>대표 이미지</span>
          </b-col>
          <b-col sm="9">
            <image-uploader
            :props_challenge_img="props_challenge_img"
            :props_challenge_img_url="props_challenge_img_url"
            props_default_img="https://homfitimage.s3.ap-northeast-2.amazonaws.com/d42ee9bafd0856a5a0b6bd481415f399"
            @imageUploaded="ImageUploaded"
            @imageUrlUploaded="ImageUrlUploaded"
            ></image-uploader>
          </b-col>
        </b-row>

        <b-row class="challenge-creating-row">
          <b-col sm="3" class="align-center">
             <span class="require-icon">*</span>
            <span>카테고리</span>
          </b-col>
          <b-col sm="9">
            <div class="align-center mx-auto">
              <input
                class="challenge_kind"
                type="radio"
                name="kind"
                :value="1"
                id="fit_id_exercise"
                v-model="kind"
              />
              <label for="fit_id_exercise">
                <span data-hover="운동">운동</span>
              </label>
              <input
                class="challenge_kind"
                type="radio"
                name="kind"
                :value="2"
                id="fit_id_diet"
                v-model="kind"
              />
              <label for="fit_id_diet">
                <span data-hover="식단">식단</span>
              </label>
            </div>
          </b-col>
        </b-row>
      </b-container>
    </div>

    <div v-if="kind == 1">
      <h4 class="challenge-creating-title">운동종류 선택</h4>
      <div class="challenge-fit align-center">
        <b-button
          :class="{ choice: EqualFitId(1), 'not-choice': NotEqualFitId(1)}"
          @click="FitId(1)"
        >
          <img src="@/assets/category/요가.png" />
          <br />
          <span>요가</span>
        </b-button>
        <b-button
          :class="{ choice: EqualFitId(2), 'not-choice': NotEqualFitId(2) }"
          @click="FitId(2)"
        >
          <img src="@/assets/category/필라테스.png" />
          <br />
          <span>필라테스</span>
        </b-button>
        <b-button
          :class="{ choice: EqualFitId(3), 'not-choice': NotEqualFitId(3) }"
          @click="FitId(3)"
        >
          <img src="@/assets/category/유산소.png" />
          <br />
          <span>유산소</span>
        </b-button>
        <b-button
          :class="{ choice: EqualFitId(4), 'not-choice': NotEqualFitId(4) }"
          @click="FitId(4)"
        >
          <img src="@/assets/category/댄스.png" />
          <br />
          <span>댄스</span>
        </b-button>
        <b-button
          :class="{ choice: EqualFitId(5), 'not-choice': NotEqualFitId(5) }"
          @click="FitId(5)"
        >
          <img src="@/assets/category/스트레칭.png" />
          <br />
          <span>스트레칭</span>
        </b-button>
        <b-button
          :class="{ choice: EqualFitId(6), 'not-choice': NotEqualFitId(6) }"
          @click="FitId(6)"
        >
          <img src="@/assets/category/근력.png" />
          <br />
          <span>근력</span>
        </b-button>
        <b-button
          :class="{ choice: EqualFitId(7), 'not-choice': NotEqualFitId(7) }"
          @click="FitId(7)"
        >
          <img src="@/assets/category/키즈.png" />
          <br />
          <span>키즈</span>
        </b-button>
        <b-button
          :class="{ choice: EqualFitId(8), 'not-choice': NotEqualFitId(8)}"
          @click="FitId(8)"
        >
          <img src="@/assets/category/복싱.png" />
          <br />
          <span>복싱</span>
        </b-button>
        <b-button
          :class="{ choice: EqualFitId(10), 'not-choice': NotEqualFitId(10) }"
          @click="FitId(10)"
        >
          <img src="@/assets/category/기타.png" />
          <br />
          <span>기타</span>
        </b-button>
      </div>

      <div class="align-center col-12 col-md-8 pb-5 mx-auto">
        <h4 class="challenge-creating-title">부위별 선택</h4>
        <input
          class="challenge-body"
          type="radio"
          name="bodyList"
          :value="1"
          v-model="bodyList"
          id="bodyList_whole"
        />
        <label for="bodyList_whole"> <span class="text">전신</span> </label
        ><input
          class="challenge-body"
          type="radio"
          name="bodyList"
          :value="2"
          v-model="bodyList"
          id="bodyList_upper"
        />
        <label for="bodyList_upper"> <span class="text">상체</span> </label
        ><input
          class="challenge-body"
          type="radio"
          name="bodyList"
          :value="3"
          v-model="bodyList"
          id="bodyList_lower"
        />
        <label for="bodyList_lower"> <span class="text">하체</span> </label
        ><input
          class="challenge-body"
          type="radio"
          name="bodyList"
          :value="4"
          v-model="bodyList"
          id="bodyList_chest"
        />
        <label for="bodyList_chest"> <span class="text">가슴</span> </label
        ><input
          class="challenge-body"
          type="radio"
          name="bodyList"
          :value="5"
          v-model="bodyList"
          id="bodyList_arm"
        />
        <label for="bodyList_arm"> <span class="text">팔</span> </label
        ><input
          class="challenge-body"
          type="radio"
          name="bodyList"
          :value="6"
          v-model="bodyList"
          id="bodyList_abdominal"
        />
        <label for="bodyList_abdominal"> <span class="text">복부</span> </label
        ><input
          class="challenge-body"
          type="radio"
          name="bodyList"
          :value="7"
          v-model="bodyList"
          id="bodyList_hip"
        />
        <label for="bodyList_hip"> <span class="text">엉덩이</span> </label>
        <input
          class="challenge-body"
          type="radio"
          name="bodyList"
          :value="8"
          v-model="bodyList"
          id="bodyList_leg"
        />
        <label for="bodyList_leg"> <span class="text">다리</span> </label>
        <input
          class="challenge-body"
          type="radio"
          name="bodyList"
          id="bodyList_etc"
          :value="9"
          v-model="bodyList"
        />
        <label for="bodyList_etc"> <span class="text">기타</span> </label>
      </div>
    </div>

    <div class="mb-4">
      <b-button
        class="next-page-button"
        :class="{ disabled: !canGoNext }"
        @click="(e) => canGoNext && NextPage()"
        aria-disabled="true"
        ><span>기간</span
        ><b-icon icon="arrow-right-circle-fill" scale="1.5"></b-icon
      ></b-button>
    </div>
  </form>
</template>

<script>
import TextEditor from "@/components/ChallengeCreating/TextEditor.vue";
import ImageUploader from "@/components/ImageUploader.vue";

export default {
  components: {
    TextEditor,
    ImageUploader
  },

  props: {
    props_kind: Number,
    props_fit_id: Number,
    props_bodyList: Array,
    props_challenge_title: String,
    props_challenge_contents: String,
    props_challenge_img: Object,
    props_challenge_img_url:String ,
  },
  data() {
    return {
      kind: 0,
      fit_id: 0,
      bodyList: 1,
      challenge_title: "",
      challenge_contents: "",
      challenge_img:null,
      challenge_img_url:'',
      canGoNext: false,
    };
  },
  created() {
    this.kind = this.props_kind;
    this.fit_id = this.props_fit_id;
    this.bodyList = this.props_bodyList[0];
    this.challenge_title = this.props_challenge_title;
    this.challenge_contents = this.props_challenge_contents;
    this.challenge_img = this.props_challenge_img;
    this.challenge_img_url = this.props_challenge_img_url;
  },
  watch: {
    fit_id: function() {
      this.CanGoNext();
    },
    bodyList: function() {
      this.CanGoNext();
    },
    challenge_title: function() {
      this.CanGoNext();
    },
    challenge_contents: function() {
      this.CanGoNext();
    },
  },
  methods: {
    ImageUploaded:function(image){
      this.challenge_img = image;
    },
     ImageUrlUploaded:function(image){
      this.challenge_img_url = image;
    },
    FitId: function(num) {
      this.fit_id = num;
    },
    GetEditorContent: function(data) {
      this.challenge_contents = data;
    },
    CanGoNext: function() {
      if (
        this.challenge_title.length > 0 &&
        this.challenge_title.length <= 20 &&
        this.challenge_contents.length > 7
      ) {
        this.canGoNext = true;
      } else {
        this.canGoNext = false;
      }
    },
    NextPage: function() {
     
      this.$emit(
        "NextPage",
        this.kind,
        this.fit_id,
        [this.bodyList],
        this.challenge_title,
        this.challenge_contents,
        this.challenge_img,
        this.challenge_img_url
      );
    },
    EqualFitId:function(value){
      return value == this.fit_id;
    },
    NotEqualFitId:function(value){
       return value != this.fit_id;
    },
  },

  computed: {
    challengeTitleState() {
      if (this.challenge_title.length == 0) return null;
      return this.challenge_title.length <= 20;
    },
    
  },
};
</script>
