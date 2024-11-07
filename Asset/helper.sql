

CREATE OR REPLACE FUNCTION reset_sku_value(_group_id VARCHAR)
  RETURNS VOID
  LANGUAGE PLPGSQL
  AS
$$
  DECLARE
  	temprow RECORD;
  	iterator float8 := 1;
BEGIN
  UPDATE product_unit SET sku = NULL WHERE group_id = _group_id;
  BEGIN FOR temprow IN
    SELECT * FROM product_unit WHERE group_id = _group_id ORDER BY id ASC
    LOOP
      UPDATE product_unit SET sku = FORMAT('SP%s', LPAD(iterator::text, 4, '0')) WHERE product_unit.id = temprow.id AND group_id = _group_id;
      iterator := iterator + 1;
    END LOOP;
  END;
END;
$$;