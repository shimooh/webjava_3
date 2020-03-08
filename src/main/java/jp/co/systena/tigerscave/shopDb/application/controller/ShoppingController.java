package jp.co.systena.tigerscave.shopDb.application.controller;

import java.util.Map;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import jp.co.systena.tigerscave.shopDb.application.controller.service.ListService;
import jp.co.systena.tigerscave.shopDb.application.model.Cart;
import jp.co.systena.tigerscave.shopDb.application.model.DeleteForm;
import jp.co.systena.tigerscave.shopDb.application.model.Item;
import jp.co.systena.tigerscave.shopDb.application.model.ListForm;
import jp.co.systena.tigerscave.shopDb.application.model.Order;

@Controller
public class ShoppingController {

    @Autowired
    HttpSession session; // セッション管理

    @Autowired
    ListService listService; // サービスクラス

    //一覧表示
    @RequestMapping(value = "/listView", method = RequestMethod.GET) //GETで情報を取得する
    public ModelAndView list(ModelAndView mav) {
        Map<Integer, Item> itemList = listService.getItemList(); //商品一覧取得

        mav.addObject("itemList", itemList); //テンプレートに渡す内容を設定
        mav.setViewName("listView"); //テンプレート名の指定

        return mav;
    }

    //カート内に追加
    @RequestMapping(value = "/listView", method = RequestMethod.POST) //POSTでポスト通信をする(情報を渡すときなどに使う)
    public ModelAndView order(ModelAndView mav, ListForm lf, BindingResult bindingResult) {

        if (bindingResult.getAllErrors().size() > 0) {
        // リクエストパラメータにエラーがある場合は商品一覧画面を表示

            Map<Integer, Item> itemListMap = listService.getItemList();
            mav.addObject("itemList", itemListMap);

            // Viewのテンプレート名を設定
            mav.setViewName("listView");
            return mav;
        }
        // 注文内容をカートに追加
        Cart cart = getCart();
        cart.addOrder(lf.getItemId(), lf.getNum());

        // データをセッションへ保存
        session.setAttribute("cart", cart);
        return new ModelAndView("redirect:/cart"); // リダイレクト
    }

    /**
     * カートの内容を表示する
     *
     * @param mav the mav
     * @return the model and view
     */
    @RequestMapping(value = "/cart", method = RequestMethod.GET) // URLとのマッピング
    public ModelAndView cart(ModelAndView mav) {

        // セッションからカートを取得し、テンプレートに渡す
        Cart cart = getCart();
        mav.addObject("orderList", cart.getOrderList());

        // 商品一覧をテンプレートに渡す。※商品名、価格を表示するため
        Map<Integer, Item> itemListMap = listService.getItemList();
        mav.addObject("itemList", itemListMap);

        // 合計金額計算
        int totalPrice = 0;
        for (Order order : cart.getOrderList()) {
            if (itemListMap.containsKey(order.getItemId())) {
                totalPrice += order.getNum() * Integer.valueOf(itemListMap.get(order.getItemId()).getPrice());
            }
        }
        mav.addObject("totalPrice", totalPrice);

        // Viewのテンプレート名設定
        mav.setViewName("cartView");
        return mav;
    }

    /**
     * 注文内容削除する
     *
     * @param mav the mav
     * @param deleteForm the delete form
     * @param bindingResult the binding result
     * @return the model and view
     */
    @RequestMapping(value = "/cart", method = RequestMethod.POST) // URLとのマッピング
    public ModelAndView deleteOrder(ModelAndView mav, @Validated DeleteForm deleteForm,
        BindingResult bindingResult) {

        if (bindingResult.getAllErrors().size() == 0) {
            //リクエストパラメータにエラーがなければ、削除処理行う

            // カートから商品を削除
            Cart cart = getCart();
            cart.deleteOrder(deleteForm.getItemId());

            // データをセッションへ保存
            session.setAttribute("cart", cart);
        }
        return new ModelAndView("redirect:/cart"); // リダイレクト
    }

    /**
     * セッションからカートを取得する
     *
     * @return the cart
     */
    private Cart getCart() {
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
            session.setAttribute("cart", cart);
        }
      return cart;
    }
}
