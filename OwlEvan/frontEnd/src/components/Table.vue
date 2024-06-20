<template>
  <v-layout class="rounded rounded-md">
    <Bar></Bar>
    <v-main>
      <v-container fluid>
        <v-row>
          <v-col cols="12" style="height: 984px">
            <v-sheet class="pa-2 ma-2 border-bottom h-100">
              <v-card variant="text" class="pt-4">
                <v-card-title>
                  <div class="text-h4 font-weight-bold">訂單查詢</div>
                </v-card-title>
                <br />
                <v-card-text class="py-0">
                  <v-table fixed-header style="font-size: 18px;">
                    <thead>
                      <tr>
                        <th class="text-left">訂單編號</th>
                        <th class="text-left">訂單金額</th>
                        <th class="text-left">狀態</th>
                        <th class="text-left">客戶</th>
                        <th class="text-left">建立日期</th>
                      </tr>
                    </thead>
                    <tbody>
                      <tr v-for="item in orders" :key="item.ordNo">
                        <td>{{ item.ordNo }}</td>
                        <td>{{ item.cost }}</td>
                        <td>{{ item.ordStatus }}</td>
                        <td>{{ item.custNo }}</td>
                        <td>{{ item.createDate }}</td>
                      </tr>
                    </tbody>
                  </v-table>
                </v-card-text>
              </v-card>
            </v-sheet>
          </v-col>
        </v-row>
      </v-container>
    </v-main>
  </v-layout>
</template>
<script>
import Bar from "./Bar.vue";

export default {
  data() {
    return {
      orders:[
        {ordNo : 1, cost : 40, ordStatus : "finish", custNo : 5566777, createDate : "20220101"},
        {ordNo : 2, cost : 50, ordStatus : "finish", custNo : 5566777, createDate : "20220101"},
      ]
    };
  },
  components: {
    Bar,
  },
};
</script>
<style scoped>
.border-bottom {
  border: 3px solid #c4c3aa;
  border-radius: 25px;
}
</style>