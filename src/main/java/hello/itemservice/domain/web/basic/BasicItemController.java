package hello.itemservice.domain.web.basic;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PostConstruct;
import java.util.List;

@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor
public class BasicItemController {

    private final ItemRepository itemRepository;

    @GetMapping
    public String items(Model model){
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "basic/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model){
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/item";
    }

    @GetMapping("/add")
    public String addForm(){

        return "basic/addForm";
    }

//    @PostMapping("/add")
    public String addItemV1(@RequestParam String itemName
                       ,@RequestParam int price
                       ,@RequestParam Integer quantity
                       ,Model model
        ){
        Item item = new Item();
        item.setItemName(itemName);
        item.setPrice(price);
        item.setQuantity(quantity);

        itemRepository.save(item);

        model.addAttribute("item", item);

        return "basic/item";
    }

    /**
     * @ModelAttribute("item") 라고 사용시 model에 item 으로 자동 추가됨
     * @param item
     * @return
     */
//    @PostMapping("/add")
    public String addItemV2(@ModelAttribute("item") Item item){
        itemRepository.save(item);

//        model.addAttribute("item", item); //따라서 생략해도 Model 에 'item' 으로 Item 객체가 담긴다.

        return "basic/item";
    }

    /**
     * 파라미터 클래스 타입의 첫번째 글자를 소문자로 변경하여 Model에 추가 (Item -> item)
     * @param item
     * @return
     */
//    @PostMapping("/add")
    public String addItemV3(@ModelAttribute Item item){
        itemRepository.save(item);

//        model.addAttribute("item", item); //자동 추가, 생략 가능

        return "basic/item";
    }

    /**
     * @ModelAttribute 생략가능
     * @param item
     * @return
     */
//    @PostMapping("/add")
    public String addItemV4(Item item){
        itemRepository.save(item);

//        model.addAttribute("item", item); //자동 추가, 생략 가능

        return "basic/item";
    }

    /**
     * 클라이언트에서 중복 post 요청을 막기위한 redirect 처리
     * @param item
     * @return
     */
//    @PostMapping("/add")
    public String addItemV5(Item item){
        itemRepository.save(item);
        return "redirect:/basic/items/" + item.getId();
    }

    /**
     * redirectAttributes 를 사용하여 인코딩뿐만 아니라 쿼리 파라미터로 url에 정보
     * @param item
     * @param redirectAttributes
     * @return
     */
    @PostMapping("/add")
    public String addItemV6(Item item, RedirectAttributes redirectAttributes){
        itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", item.getId());
        redirectAttributes.addAttribute("status", true); // 쿼리 파라미터 형식으로 들어감 (?status=true)
        return "redirect:/basic/items/{itemId}";
    }

    @GetMapping("{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model){
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "/basic/editForm";
    }

    @PostMapping("{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item){
        itemRepository.update(itemId, item);
        return "redirect:/basic/items/{itemId}";
    }

    /**
     * 테스트용 데이터
     */
    @PostConstruct
    public void init(){
        itemRepository.save(new Item("itemA", 10000, 10));
        itemRepository.save(new Item("itemB", 20000, 20));
    }

}
