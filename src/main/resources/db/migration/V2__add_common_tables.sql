create or replace view medicine_derived as
select m.id                              as id,
       count(*) OVER (PARTITION BY m.id) as count_in_stock
from medicine_item item
         left join medicine m on item.medicine_id = m.id
WHERE item.sales = false
  and item.purchase_order_id is not null
group by m.id;

create or replace view shopping_cart_item_derived as
select scm.id                 as id,
       coalesce(eo.number, 0) as external_quantity,
       coalesce(mo.number, 0) as manufacturing_quantity
from shopping_cart_medicine scm
         left join external_order eo on scm.medicine_id = eo.medicine_id
         left join manufacturing_order mo on scm.medicine_id = mo.medicine_id
where (eo.purchase_order_id = scm.purchase_order_id or eo.purchase_order_id IS NULL)
  and (mo.purchase_order_id = scm.purchase_order_id or mo.purchase_order_id IS NULL)
