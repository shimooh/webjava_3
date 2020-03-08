package jp.co.systena.tigerscave.shopDb.application.controller.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import jp.co.systena.tigerscave.shopDb.application.model.Item;

@Service
public class ListService {
  @Autowired
  JdbcTemplate jdbcTemplate;

  public Map<Integer, Item> getItemList() {
    Map<Integer, Item> itemListMap = new LinkedHashMap<Integer, Item>();
    for(int i = 0; i < getItems().size(); i++) {
      itemListMap.put(i, getItems().get(i));
    }

    return itemListMap;
  }
  /**
   * データベースからアイテムデータ一覧を取得する
   *
   * @return
   */
  private List<Item> getItems() {
      //SELECTを使用してテーブルの情報をすべて取得する
      List<Item> list = jdbcTemplate.query("SELECT * FROM items ORDER BY item_id", new BeanPropertyRowMapper<Item>(Item.class));
      return list;
  }
}
