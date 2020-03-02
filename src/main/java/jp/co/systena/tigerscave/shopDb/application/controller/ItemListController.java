package jp.co.systena.tigerscave.shopDb.application.controller;

import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import jp.co.systena.tigerscave.shopDb.application.model.Item;
import jp.co.systena.tigerscave.shopDb.application.model.ItemListForm;

@Controller
public class ItemListController {

    @Autowired
    JdbcTemplate jdbcTemplate;

    /**
     * 初期表示用
     * アイテムデータを取得して一覧表示する
     *
     * @param model
     * @return アイテムリスト
     */
    @RequestMapping(value = "/itemlist", method = RequestMethod.GET)
    public String index(Model model) {

    model.addAttribute("items", getItemList());

    return "itemlist";
  }



    /**
     * 「更新」ボタン押下時の処理
     * 入力された名前と価格をアイテムIDをキーとして更新する
     *
     * @param listForm
     * @param result
     * @param model
     * @return
     */
    @RequestMapping(value = "/itemlist", method = RequestMethod.POST) // URLとのマッピング
    public String update(@Valid ItemListForm listForm,
                        BindingResult result,
                        Model model) {

        // listFormに画面で入力したデータが入っているので取得する
        List<Item> itemList = listForm.getItemList();
        // ビューに受け渡し用にmodelにセット
        model.addAttribute("items", itemList);
        model.addAttribute("listForm", listForm);

        if (!result.hasErrors()) {
            if (itemList != null) {
                for (Item item : itemList) {

                    int updateCount = jdbcTemplate.update(
                      "UPDATE items SET item_name = ?, price = ? WHERE item_id = ?",
                      item.getItemName(),
                      Integer.parseInt(item.getPrice()),
                      Integer.parseInt(item.getItemId()));

                }
            }
        }
        return "itemlist";
    }

    /**
     * 「削除」リンク押下時の処理
     * パラメータで受け取ったアイテムIDのデータを削除する
     *
     * @param itemId
     * @param model
     * @return
     */
    @RequestMapping(value = "/deleteitem", method = RequestMethod.GET) // URLとのマッピング
    public String update(@RequestParam(name = "item_id", required = true) String itemId,
        Model model) {
        // 入力チェックを入れる？
        int deleteCount = jdbcTemplate.update("DELETE FROM items WHERE item_id = ?", Integer.parseInt(itemId));
        return "redirect:/itemlist";

    }

    /**
     * 「登録」ボタン押下時の処理
     * 入力されたアイテムID、名前、価格をデータベースに登録する
     *
     * @param form
     * @param result
     * @param model
     * @return
     */
    @RequestMapping(value = "/additem", method = RequestMethod.POST) // URLとのマッピング
    public String insert(@Valid Item form,
                        BindingResult result,
                        Model model) {

        if (!result.hasErrors()) {
            int insertCount = jdbcTemplate.update(
                "INSERT INTO items VALUES( ?, ?, ? )",
                Integer.parseInt(form.getItemId()),
                form.getItemName(),
                Integer.parseInt(form.getPrice())
            );
        }
        return "redirect:/itemlist";
    }

    /**
     * データベースからアイテムデータ一覧を取得する
     *
     * @return
     */
    private List<Item> getItemList() {
        //SELECTを使用してテーブルの情報をすべて取得する
        List<Item> list = jdbcTemplate.query("SELECT * FROM items ORDER BY item_id", new BeanPropertyRowMapper<Item>(Item.class));

        return list;
    }
}
