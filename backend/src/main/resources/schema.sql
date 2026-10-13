CREATE TABLE IF NOT EXISTS public.card (
    id text NOT NULL,
    private_id int4 NULL,
    name text NULL,
    lang text NULL,
    count int4 NULL,
    set text NULL,
    set_name text NULL,
    set_id text NULL,
    collector_number text NULL,
    released_at date NULL,
    type_line text NULL,
    oracle_text text NULL,
    mana_cost text NULL,
    cmc float8 NULL,
    colors _text NULL,
    color_identity _text NULL,
    keywords _text NULL,
    rarity text NULL,
    layout text NULL,
    reserved bool NULL,
    reprint bool NULL,
    full_art bool NULL,
    booster bool NULL,
    border_color text NULL,
    edhrec_rank int4 NULL,
    artist text NULL,
    card_back_id text NULL,
    price_eur float8 NULL,
    image_png text NULL,
    image_small text NULL,
    image_normal text NULL,
    image_png_data bytea NULL,
    image_small_data bytea NULL,
    image_normal_data bytea NULL,
    purchase_cardmarket text NULL,
    oracle_id text NULL,
    CONSTRAINT card_pkey PRIMARY KEY (id)
);

-- Indizes für die Filter in der Sammlungsansicht
CREATE INDEX IF NOT EXISTS idx_card_rarity ON public.card (rarity);
CREATE INDEX IF NOT EXISTS idx_card_cmc    ON public.card (cmc);
CREATE INDEX IF NOT EXISTS idx_card_colors ON public.card USING GIN (colors);