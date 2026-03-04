create table shipment_view (
    shipment_id uuid primary key,
    order_id uuid,
    shipment_created_at timestamptz,
    label_id text null,
    label_created_at timestamptz null,
    status text not null, -- CREATED, LABELED
    updated_at timestamptz not null
);