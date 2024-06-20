<template>
  <v-layout class="rounded rounded-md">
    <Bar></Bar>
    <v-main>
      <v-container fluid>
        <v-row>
          <v-col cols="12" style="height: 493px">
            <v-sheet class="pa-2 ma-2 border-bottom h-100">
              <v-row no-gutters style="height: 291px">
                <v-col class="h-100" cols="4">
                  <v-card variant="text" class="pt-4">
                    <v-card-title>
                      <div class="text-h4 font-weight-bold">本周營收</div>
                    </v-card-title>
                    <br />
                    <v-card-text class="py-0">
                      <div class="text-h2 font-weight-bold">
                        ${{ weekEarned }}
                      </div>
                      <br />
                      <v-chip class="ma-2" color="green" size="x-large">
                        <template v-slot:prepend>
                          <v-icon icon="mdi-arrow-up-right" />
                        </template>
                        <v-container>{{ upper }}%</v-container>
                      </v-chip>
                    </v-card-text>
                  </v-card>
                </v-col>
                <v-col class="h-100" cols="8">
                  <v-sheet class="pa-2 h-100" style="background-color: #dedede">
                    還沒串
                  </v-sheet>
                </v-col>
              </v-row>
              <v-row no-gutters style="height: 122px">
                <v-col
                  class="h-100"
                  cols="4"
                  v-for="(item, i) in dashBoards"
                  :key="i"
                >
                  <v-container>
                    <v-card variant="text" :class="{ 'border-e-lg': i < 2 }">
                      <v-card-title class="pa-0 ma-0">
                        <div class="text-h5 font-weight-bold">
                          {{ item.title }}
                        </div>
                      </v-card-title>
                      <br />
                      <v-card-text>
                        <div class="text-h3 font-weight-bold">
                          {{ item.index }}
                        </div>
                      </v-card-text>
                    </v-card>
                  </v-container>
                </v-col>
              </v-row>
            </v-sheet>
          </v-col>
        </v-row>
        <v-row>
          <v-col cols="9" style="height: 493px">
            <v-sheet class="pa-2 ma-2 border-bottom h-100">
              <v-container>
                <v-card variant="text">
                  <v-card-title>
                    <div class="text-h4 font-weight-bold">商品銷售量 TOP 5</div>
                  </v-card-title>
                  <br />
                  <v-card-text>
                    <v-sheet
                      class="d-flex align-center"
                      v-for="(item, i) in linerColors"
                      :key="i"
                    >
                      <v-col cols="1"><div class="text-h6">3/9</div></v-col>
                      <v-progress-linear
                        :color="item.color"
                        height="22"
                        :model-value="item.index"
                        rounded="lg"
                      >
                      </v-progress-linear>
                    </v-sheet>
                  </v-card-text>
                </v-card>
              </v-container>
            </v-sheet>
          </v-col>
          <v-col cols="3" style="height: 493px">
            <v-sheet class="pa-2 ma-2 border-bottom h-100">
              <v-container>
                <v-card variant="text">
                  <v-card-title class="d-flex justify-space-between">
                    <div class="text-h4 font-weight-bold">代辦事項</div>
                    <v-icon @click="showInput = !showInput"
                      >mdi-plus-box</v-icon
                    >
                  </v-card-title>
                  <br />
                  <v-card-text class="pa-0">
                    <v-list select-strategy="classic">
                      <v-list-item v-for="(item, index) in items.slice(0,5)" :key="index">
                        <template v-slot:prepend="">
                          <v-list-item-action start>
                            <v-checkbox-btn
                              v-model="item.completed"
                              @change="updateCompleted(index, item.completed)"
                            ></v-checkbox-btn>
                          </v-list-item-action>
                        </template>
                        <v-list-item-title
                          >{{ item.text }}</v-list-item-title
                        >
                      </v-list-item>
                    </v-list>
                  </v-card-text>
                </v-card>

                <v-dialog v-model="showInput" max-width="500px">
                  <v-card>
                    <v-card-title>新增事項</v-card-title>
                    <v-card-text>
                      <v-text-field
                        v-model="newText"
                        label="輸入事項"
                      ></v-text-field>
                    </v-card-text>
                    <v-card-actions>
                      <v-btn @click="saveText" color="primary">保存</v-btn>
                      <v-btn @click="cancel" color="error">取消</v-btn>
                    </v-card-actions>
                  </v-card>
                </v-dialog>
              </v-container>
            </v-sheet>
          </v-col>
        </v-row>
      </v-container>
    </v-main>
  </v-layout>
</template>
<script>
import { it } from "vuetify/locale";
import Bar from "./Bar.vue";

export default {
  data() {
    return {
      weekEarned: 166666,
      upper: 55,
      linerColors: [
        { color: "green-darken-3", index: "12" },
        { color: "purple-darken-3", index: "24" },
        { color: "yellow-darken-3", index: "36" },
        { color: "blue-darken-3", index: "60" },
        { color: "red-darken-3", index: "99" },
      ],
      dashBoards: [
        { title: "成交訂單", index: "12筆" },
        { title: "測試1", index: "24" },
        { title: "測試2", index: "36" },
      ],
      items: [],
      showInput: false,
      newText: "",
    };
  },
  components: {
    Bar,
  },
  mounted() {
    const savedItems = localStorage.getItem("items");
    if (savedItems) {
      this.items = JSON.parse(savedItems);
    }
  },
  methods: {
    saveText() {
      if (this.newText.trim() !== "") {
        this.items.push({
          text: this.newText,
          completed: false,
        });
        localStorage.setItem("items", JSON.stringify(this.items));
        this.newText = "";
        this.showInput = false;
      }
    },
    cancel() {
      this.newText = "";
      this.showInput = false;
    },
    updateCompleted(index, completed) {
      this.items[index].completed = completed;
      this.updateLocalStorage();
    },
    updateLocalStorage() {
      localStorage.setItem("items", JSON.stringify(this.items));
    },
  },
};
</script>
<style scoped>
.border-bottom {
  border: 3px solid #c4c3aa;
  border-radius: 25px;
}
</style>